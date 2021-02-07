package dev.ghost.homework_2.presentation.post

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dev.ghost.homework_2.model.entities.Post
import dev.ghost.homework_2.model.entities.PostWithOwner
import dev.ghost.homework_2.model.models.ResponseContent
import dev.ghost.homework_2.model.repositories.GroupRepository
import dev.ghost.homework_2.model.repositories.PhotoRepository
import dev.ghost.homework_2.model.repositories.PostsRepository
import dev.ghost.homework_2.presentation.base.mvp.presenter.RxPresenter
import dev.ghost.homework_2.presentation.Action
import dev.ghost.homework_2.presentation.State
import dev.ghost.homework_2.presentation.UiEffect
import dev.ghost.homework_2.presentation.reduce
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

private typealias PostSideEffect = SideEffect<State, out Action>

class PostsPresenter(
    private val postsRepository: PostsRepository,
    private val groupsRepository: GroupRepository,
    private val photosRepository: PhotoRepository,
    isFavorite: Boolean = false
) : RxPresenter<AdapterView>(AdapterView::class.java) {

    private val data: Observable<List<PostWithOwner>>
    private val compositeDisposable = CompositeDisposable()

    private var isFirstLoading = true

    init {
        val now = GregorianCalendar()
        now.add(GregorianCalendar.DAY_OF_YEAR, -1)
        data = if (isFavorite) postsRepository.getFavouritePosts()
            .observeOn(Schedulers.io())
            .toObservable()
            .debounce(1, TimeUnit.SECONDS)
        else postsRepository.getPostsAfterDate(now.timeInMillis / 1000)
            .observeOn(Schedulers.io())
            .toObservable()
            .debounce(1, TimeUnit.SECONDS)
    }

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
                .switchMap {
                    getDataFromDB()
                        .onErrorReturn { error ->
                            Action.ErrorLoadData(error)
                        }
                }
        }
    }

    private fun getDataFromDB(): Observable<Action> {
        return data
            .map { items ->
                if (isFirstLoading) {
                    isFirstLoading = false
                    input.accept(Action.LoadDataFromNetwork)
                }
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
        return postsRepository.refresh().subscribeOn(Schedulers.io())
            .toObservable()
            .map { vkResponse ->
                if (vkResponse.isSuccessful) {
                    vkResponse.body()?.response?.let {
                        updateReceivedContent(it)
                    }
                    return@map Action.DataRefreshed
                } else {
                    return@map Action.ErrorLoadData(Throwable(vkResponse.errorBody()?.string()))
                }
            }
    }

    private fun updateReceivedContent(content: ResponseContent) {
        groupsRepository.updateReceivedGroups(content.groups)
        val currentItems = content.items.filter {
            it.sourceId < 0
        }
        postsRepository.updateReceivedPosts(currentItems)
        currentItems.forEach { post ->
            post.attachments?.let {
                photosRepository.updateReceivedPhotos(it, post.postId)
            }
        }
    }

    fun hidePost(postWithOwner: PostWithOwner) {
        val disposable =
            postsRepository.hidePostAsync(postWithOwner.post.id, postWithOwner.getOwnerId())
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                    onSuccess = { response ->
                        if (response.isSuccessful) {
                            response.body()?.let { responseBody ->
                                if (responseBody.response == 1) {
                                    deletePost(postWithOwner.post)
                                }
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

    fun likePost(postId: Int, ownerId: Int, isLiked: Boolean) {
        val disposable = postsRepository.likePostAsync(postId, ownerId, isLiked)
            .subscribeOn(Schedulers.io())
            .subscribeBy(onSuccess = { response ->
                if (response.isSuccessful) {
                    var currentLikes = -1
                    response.body()?.response?.likes?.let { likes ->
                        currentLikes = likes
                    }
                    postsRepository.changeLikes(postId, isLiked, currentLikes)
                } else
                    error(response.errorBody()!!.string())
            },
                onError = { error ->
                    uiEffectsRelay.accept(UiEffect.ActionError(error))
                }
            )
        compositeDisposable.add(disposable)
    }

    fun showPostDetails(postWithOwner: PostWithOwner) {
        uiEffectsRelay.accept(UiEffect.ShowPostDetails(postWithOwner))
    }

    fun showPostImages(postWithOwner: PostWithOwner) {
        uiEffectsRelay.accept(UiEffect.ShowPostImages(postWithOwner))
    }

    private fun transformContent(responseContent: ResponseContent, ownerId: Int, postId: Int) {
        responseContent.items.map {
            it.sourceId = ownerId
            it.postId = postId
        }
        updateReceivedContent(responseContent)
    }

    private fun deletePost(post: Post) = postsRepository.deletePost(post)

    override fun detachView(isFinishing: Boolean) {
        super.detachView(isFinishing)
        if (isFinishing)
            compositeDisposable.clear()
    }
}

