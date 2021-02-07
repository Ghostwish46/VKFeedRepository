package dev.ghost.homework_2.presentation.newPost

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dev.ghost.homework_2.model.repositories.*
import dev.ghost.homework_2.presentation.base.mvp.presenter.RxPresenter
import dev.ghost.homework_2.presentation.Action
import dev.ghost.homework_2.presentation.State
import dev.ghost.homework_2.presentation.UiEffect
import dev.ghost.homework_2.presentation.post.AdapterView
import dev.ghost.homework_2.presentation.reduce
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

private typealias PostSideEffect = SideEffect<State, out Action>

class NewPostPresenter(
    private val postsRepository: PostsRepository
) : RxPresenter<AdapterView>(AdapterView::class.java) {

    private val compositeDisposable = CompositeDisposable()

    private val inputRelay: Relay<Action> = PublishRelay.create()
    private val uiEffectsRelay = PublishRelay.create<UiEffect>()

    val input: Consumer<Action> get() = inputRelay
    private val uiEffectsInput: Observable<UiEffect> get() = uiEffectsRelay

    private val state: Observable<State> = inputRelay.reduxStore(
        initialState = State(),
        sideEffects = listOf(),
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

    fun sendPost(message: String) {
        if (message.isEmpty()) {
            uiEffectsRelay.accept(UiEffect.ActionError(Throwable("Message can not be empty")))
            return
        }

        val disposable = postsRepository.sendWallPostAsync(message)
            .subscribeOn(Schedulers.io())
            .subscribeBy(onSuccess = { response ->
                if (response.isSuccessful) {
                    uiEffectsRelay.accept(UiEffect.ObjectSent)
                    response.body()?.createWallPostResponseContent?.postId?.let {
                        uiEffectsRelay.accept(UiEffect.ObjectSent)
                    }
                } else
                    error(response.errorBody()!!.string())
            },
                onError = { error ->
                    uiEffectsRelay.accept(UiEffect.ActionError(error))
                }
            )
        compositeDisposable.add(disposable)
    }

    override fun detachView(isFinishing: Boolean) {
        super.detachView(isFinishing)
        if (isFinishing)
            compositeDisposable.clear()
    }
}

