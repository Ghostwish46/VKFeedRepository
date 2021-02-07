package dev.ghost.homework_2.presentation.profile

import com.freeletics.rxredux.SideEffect
import com.freeletics.rxredux.reduxStore
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dev.ghost.homework_2.model.entities.PostWithOwner
import dev.ghost.homework_2.model.models.*
import dev.ghost.homework_2.model.network.ApiVariables
import dev.ghost.homework_2.model.repositories.GroupRepository
import dev.ghost.homework_2.model.repositories.PhotoRepository
import dev.ghost.homework_2.model.repositories.PostsRepository
import dev.ghost.homework_2.model.repositories.ProfileRepository
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

class ProfilePresenter(
    private val postsRepository: PostsRepository,
    private val groupsRepository: GroupRepository,
    private val photosRepository: PhotoRepository,
    private val profileRepository: ProfileRepository,
) : RxPresenter<AdapterView>(AdapterView::class.java) {

    private val profileData = profileRepository
        .getUserProfile(ApiVariables.USER_ID)
        .subscribeOn(Schedulers.io())
    private val wallData = postsRepository
        .getAllWallPosts()
        .subscribeOn(Schedulers.io())

    private val compositeDisposable = CompositeDisposable()
    private var isFirstLoading = true

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
        return Observable.combineLatest(profileData.toObservable(), wallData.toObservable(),
            { profiles, wall ->
                val itemsList: MutableList<AdapterItem> = mutableListOf()
                profiles.forEach { itemsList.add(it) }
                wall.forEach { itemsList.add(it) }
                return@combineLatest itemsList
            }
        )
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
        return profileRepository.refresh()
            .subscribeOn(Schedulers.io())
            .flatMap { response ->
                if (response.isSuccessful) {
                    response.body()?.userResponseContent?.firstOrNull()?.let {
                        profileRepository.addUser(it)
                    }
                }
                postsRepository.refreshWall()
            }
            .toObservable()
            .map { wallResponse ->
                if (wallResponse.isSuccessful) {
                    wallResponse.body()?.wallResponseContent?.let {
                        updateReceivedContent(it)
                    }
                    Action.DataRefreshed
                } else
                    Action.ErrorLoadData(Throwable(wallResponse.errorBody()?.string().toString()))
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

    fun showPostDetails(postWithOwner: PostWithOwner) {
        uiEffectsRelay.accept(UiEffect.ShowPostDetails(postWithOwner))
    }

    fun showPostImages(postWithOwner: PostWithOwner) {
        uiEffectsRelay.accept(UiEffect.ShowPostImages(postWithOwner))
    }

    private fun updateReceivedContent(content: WallResponseContent) {
        groupsRepository.updateReceivedGroups(content.groups)
        profileRepository.updateReceivedProfiles(content.profiles)
        postsRepository.updateReceivedWall(content.wallItems)

        content.wallItems.forEach { wall ->
            wall.attachments?.let {
                photosRepository.updateReceivedPhotos(it, wall.id)
            }
        }
    }

    override fun detachView(isFinishing: Boolean) {
        super.detachView(isFinishing)
        if (isFinishing)
            compositeDisposable.clear()
    }
}

