package dev.ghost.homework_2.presentation.images

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dev.ghost.homework_2.R
import dev.ghost.homework_2.model.entities.Photo
import dev.ghost.homework_2.model.repositories.PhotoRepository
import dev.ghost.homework_2.presentation.Action
import dev.ghost.homework_2.presentation.State
import dev.ghost.homework_2.presentation.UiEffect
import dev.ghost.homework_2.presentation.base.mvp.presenter.RxPresenter
import dev.ghost.homework_2.presentation.post.AdapterView
import dev.ghost.homework_2.presentation.reduce
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

private typealias PostSideEffect = SideEffect<State, out Action>

class ImagesPresenter(
    photoRepository: PhotoRepository,
    postId: Int,
    val context: Context
) : RxPresenter<AdapterView>(AdapterView::class.java) {
    private val imagesData = photoRepository.getPhotosByPost(postId)
        .subscribeOn(Schedulers.io())
        .toObservable()

    private val compositeDisposable = CompositeDisposable()
    private val formatter =
        SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.UK)

    private val inputRelay: Relay<Action> = PublishRelay.create()
    private val uiEffectsRelay = PublishRelay.create<UiEffect>()

    val input: Consumer<Action> get() = inputRelay
    private val uiEffectsInput: Observable<UiEffect> get() = uiEffectsRelay

    private val state: Observable<State> = inputRelay.reduxStore(
        initialState = State(),
        sideEffects = listOf(loadData()),
        reducer = State::reduce
    )

    override fun attachView(view: AdapterView) {
        super.attachView(view)
        state.observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::render)
            .disposeOnFinish()
        uiEffectsInput.observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::handleUiEffect)
            .disposeOnFinish()
    }

    private fun loadData(): PostSideEffect {
        return { actions, state ->
            actions.ofType(Action.LoadDataFromDB::class.java)
                .switchMap {
                    getDataFromDB()
                        .onErrorReturn { error ->
                            Action.ErrorLoadData(error)
                        }
                }
        }
    }

    fun downloadImage() {
        uiEffectsRelay.accept(UiEffect.DownloadImage)
    }

    fun shareImage() {
        uiEffectsRelay.accept(UiEffect.ShareImage)
    }

    private fun getDataFromDB(): Observable<Action> {
        return imagesData
            .map { items ->
                Action.AdapterDataLoaded(items)
            }
    }

    fun initLoading(currentItem: Photo, isDownloading: Boolean) {
        Glide.with(context)
            .asBitmap()
            .load(currentItem.url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    try {
                        if (isDownloading) {
                            saveImageToGallery(resource)
                        } else {
                            shareImage(resource)
                        }

                    } catch (ex: java.lang.Exception) {
                        uiEffectsRelay.accept(UiEffect.ActionError(Throwable(ex.message.toString())))
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    fun shareImage(resource: Bitmap) {
        val now = Date()
        val fileName: String = formatter.format(now) + ".png"

        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val stream =
            FileOutputStream("$cachePath/$fileName")
        resource.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()
        val newFile = File(cachePath, fileName)
        val contentUri =
            FileProvider.getUriForFile(context, "dev.ghost.homework_2.fileprovider", newFile)
        if (contentUri != null) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
                setDataAndType(contentUri, context.contentResolver.getType(contentUri))
                putExtra(Intent.EXTRA_STREAM, contentUri)
            }
            uiEffectsRelay.accept(UiEffect.CreateIntent(shareIntent))
        }
    }

    private fun saveImageToGallery(resource: Bitmap) {
        val now = Date()
        val fileName: String = formatter.format(now) + ".png"

        val file =
            File(
                Environment.getExternalStorageDirectory().toString() + "/Download/",
                fileName
            )
        saveFile(resource, file)
        context.sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + file.absolutePath)
            )
        )
        uiEffectsRelay.accept(UiEffect.ShowMessage(context.getString(R.string.info_file_saved) + file.toString()))
    }

    private fun saveFile(resource: Bitmap, file: File) {
        val stream = FileOutputStream(file)
        resource.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
    }


    override fun detachView(isFinishing: Boolean) {
        super.detachView(isFinishing)
        if (isFinishing)
            compositeDisposable.clear()
    }
}