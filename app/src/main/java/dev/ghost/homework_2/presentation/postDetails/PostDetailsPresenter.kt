package dev.ghost.homework_2.presentation.postDetails

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dev.ghost.homework_2.model.entities.*
import dev.ghost.homework_2.model.models.*
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

class PostDetailsPresenter(
    private val postsRepository: PostsRepository,
    private val groupsRepository: GroupRepository,
    private val profileRepository: ProfileRepository,
    private val commentRepository: CommentRepository,
    private val postId: Int
) : RxPresenter<AdapterView>(AdapterView::class.java) {
    private var ownerId: Int = 0

    private var isFirstLoading = true

    private val postData = postsRepository
        .getPostById(postId)
        .subscribeOn(Schedulers.io())
    private val commentsData = commentRepository
        .getCommentsByPost(postId)
        .subscribeOn(Schedulers.io())

    private val compositeDisposable = CompositeDisposable()

    private val inputRelay: Relay<Action> = PublishRelay.create()
    private val uiEffectsRelay = PublishRelay.create<UiEffect>()

    val input: Consumer<Action> get() = inputRelay
    private val uiEffectsInput: Observable<UiEffect> get() = uiEffectsRelay

    private val state: Observable<State> = inputRelay.reduxStore(
        initialState = State(),
        sideEffects = listOf(loadData(), refreshData()),
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
                .distinctUntilChanged()
                .switchMap {
                    getDataFromDB()
                        .onErrorReturn { error ->
                            Action.ErrorLoadData(error)
                        }
                }
        }
    }

    private fun getDataFromDB(): Observable<Action> {
        return Observable.combineLatest(postData.toObservable(), commentsData.toObservable(),
            { posts, comments ->
                val itemsList: MutableList<AdapterItem> = mutableListOf()
                posts.firstOrNull()?.let {
                    ownerId = it.getOwnerId()
                    if (!it.post.canComment)
                        uiEffectsRelay.accept(UiEffect.DisabledComments)
                }
                posts.forEach { itemsList.add(it) }
                comments.forEach { itemsList.add(it) }
                return@combineLatest itemsList
            }
        )
            .map { items ->
                if (isFirstLoading) {
                    isFirstLoading = false
                    input.accept(Action.LoadDataFromNetwork)
                }
                uiEffectsRelay.accept(UiEffect.ScrollToEnd)
                Action.AdapterDataLoaded(items)
            }
    }

    private fun refreshData(): PostSideEffect {
        return { actions, state ->
            actions.ofType(Action.LoadDataFromNetwork::class.java)
                .switchMap {
                    getDataFromNetwork()
                        .onErrorReturn { error ->
                            Action.ErrorLoadData(error)
                        }
                }
        }
    }

    private fun getDataFromNetwork(): Observable<Action> {
        return commentRepository.refresh(postId, ownerId)
            .subscribeOn(Schedulers.io())
            .toObservable()
            .map { commentsResponse ->
                if (commentsResponse.isSuccessful) {
                    commentsResponse.body()?.commentResponseContent?.let {
                        updateReceivedContent(it)
                    }
                    Action.DataRefreshed
                } else
                    Action.ErrorLoadData(
                        Throwable(
                            commentsResponse.errorBody()?.string().toString()
                        )
                    )
            }
    }

    fun likePost(postId: Int, ownerId: Int, isLiked: Boolean) {
        val disposable = postsRepository.likePostAsync(postId, ownerId, isLiked)
            .subscribeOn(Schedulers.io())
            .subscribeBy(onSuccess = { response ->
                if (response.isSuccessful)
                    response.body()?.response?.likes?.let { likes ->
                        postsRepository.changeLikes(postId, isLiked, likes)
                    }
                else
                    error(response.errorBody()!!.string())
            },
                onError = { error ->
                    uiEffectsRelay.accept(UiEffect.ActionError(error))
                }
            )
        compositeDisposable.add(disposable)
    }


    fun showPostImages(postWithOwner: PostWithOwner) {
        uiEffectsRelay.accept(UiEffect.ShowPostImages(postWithOwner))
    }

    fun sendComment(message: String) {
        if (message.isEmpty()) {
            uiEffectsRelay.accept(UiEffect.ActionError(Throwable("Message can not be empty")))
            return
        }

        val disposable = commentRepository.sendComment(postId, ownerId, message)
            .subscribeOn(Schedulers.io())
            .subscribeBy(onSuccess = { response ->
                if (response.isSuccessful) {
                    uiEffectsRelay.accept(UiEffect.ObjectSent)
                    response.body()?.createCommentResponseContent?.commentId?.let {

                        input.accept(Action.LoadDataFromNetwork)
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

    private fun updateReceivedContent(content: CommentResponseContent) {
        groupsRepository.updateReceivedGroups(content.groups)
        profileRepository.updateReceivedProfiles(content.profiles)
        commentRepository.updateReceivedComments(content.items)
    }

    override fun detachView(isFinishing: Boolean) {
        super.detachView(isFinishing)
        if (isFinishing)
            compositeDisposable.clear()
    }
}

