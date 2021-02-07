package dev.ghost.homework_2.presentation.images

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import dev.ghost.homework_2.R
import dev.ghost.homework_2.model.entities.Photo
import dev.ghost.homework_2.presentation.Action
import dev.ghost.homework_2.presentation.State
import dev.ghost.homework_2.presentation.UiEffect
import dev.ghost.homework_2.presentation.base.mvp.MvpActivity
import dev.ghost.homework_2.presentation.post.AdapterView
import dev.ghost.homework_2.presentation.postDetails.PostDetailsActivity
import kotlinx.android.synthetic.main.activity_images.*
import java.lang.Exception

class ImagesActivity : MvpActivity<AdapterView, ImagesPresenter>(), AdapterView {
    companion object {
        const val POST_DATA = "postData"
        const val DOWNLOADING_CODE = 1001
        const val SHARING_CODE = 1002
    }

    private lateinit var imagesViewModel: ImagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images)
        setSupportActionBar(findViewById(R.id.toolbar))
        imagesViewModel = ViewModelProvider(this).get(ImagesViewModel::class.java)

        viewPagerImages.adapter = imagesViewModel.getImagesAdapter()

        val currentPostId =
            intent.extras?.getInt(PostDetailsActivity.POST_DATA)
        currentPostId?.let {
            imagesViewModel.setPostId(currentPostId)
        }

        imageViewImageDownload.setOnClickListener {
            getPresenter().downloadImage()
        }

        imageViewImageShare.setOnClickListener {
            getPresenter().shareImage()
        }
    }

    override fun onResume() {
        super.onResume()
        getPresenter().input.accept(Action.LoadDataFromDB)
    }

    override fun getPresenter(): ImagesPresenter {
        return imagesViewModel.getPresenter()
    }

    override fun getMvpView(): AdapterView {
        return this
    }

    override fun render(state: State) {
        if (state.items.isNotEmpty())
            imagesViewModel.getImagesAdapter().submitList(state.items as List<Photo>)

        state.error?.let {
            showMessage(it.message.toString())
        }
    }

    override fun handleUiEffect(uiEffect: UiEffect) {
        when (uiEffect) {
            is UiEffect.ActionError -> {
                showMessage(uiEffect.error.toString())
            }
            is UiEffect.ShowMessage -> {
                showMessage(uiEffect.message)
            }
            is UiEffect.DownloadImage -> {
                downloadImage()
            }
            is UiEffect.ShareImage -> {
                shareImage()
            }
            is UiEffect.CreateIntent -> {
                startActivity(uiEffect.intent)
            }
            else -> {
            }
        }
    }


    private fun shareImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val currentItem =
                    imagesViewModel.getImagesAdapter().getCurrentItem(viewPagerImages.currentItem)
                getPresenter().initLoading(currentItem, false)
            } catch (ex: Exception) {
                showMessage(ex.message.toString())
            }
        } else {
            ActivityCompat
                .requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    SHARING_CODE
                )
        }
    }

    private fun downloadImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val currentItem =
                    imagesViewModel.getImagesAdapter().getCurrentItem(viewPagerImages.currentItem)
                getPresenter().initLoading(currentItem, true)
            } catch (ex: Exception) {
                showMessage(ex.message.toString())
            }
        } else {
            ActivityCompat
                .requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    DOWNLOADING_CODE
                )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                val currentItem =
                    imagesViewModel.getImagesAdapter().getCurrentItem(viewPagerImages.currentItem)

                if (requestCode == DOWNLOADING_CODE) {
                    getPresenter().initLoading(currentItem, true)
                }
                if (requestCode == SHARING_CODE) {
                    getPresenter().initLoading(currentItem, false)
                }
            } catch (ex: Exception) {
                showMessage(ex.message.toString())
            }
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}