package dev.ghost.homework_2.presentation.post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import dev.ghost.homework_2.R
import dev.ghost.homework_2.presentation.base.mvp.MvpFragment
import dev.ghost.homework_2.presentation.Action
import dev.ghost.homework_2.presentation.State
import dev.ghost.homework_2.presentation.UiEffect
import dev.ghost.homework_2.presentation.adapters.PostsViewModelFactory
import dev.ghost.homework_2.presentation.adapters.PostTouchHelperCallback
import dev.ghost.homework_2.presentation.images.ImagesActivity
import dev.ghost.homework_2.presentation.postDetails.PostDetailsActivity
import kotlinx.android.synthetic.main.fragment_posts.*
import kotlinx.android.synthetic.main.layout_empty_state.*

class PostsFragment : MvpFragment<AdapterView, PostsPresenter>(), AdapterView {

    private lateinit var postsViewModel: PostsViewModel

    override fun getPresenter() = postsViewModel.getPostsPresenter()

    override fun getMvpView() = this

    var isFavourite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (arguments != null) {
            isFavourite = arguments?.get("isFavourite").toString() == "true"
        }
        postsViewModel =
            ViewModelProvider(
                this,
                PostsViewModelFactory(requireActivity().application, isFavourite)
            ).get(PostsViewModel::class.java)

        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerPosts.adapter = postsViewModel.getPostsAdapter()
        val callback = PostTouchHelperCallback(postsViewModel.getPostsAdapter())
        val postTouchHelperAdapter = ItemTouchHelper(callback)
        postTouchHelperAdapter.attachToRecyclerView(recyclerPosts)

        swipeRefreshPosts.setOnRefreshListener {
            postsViewModel.getPostsPresenter().input.accept(Action.LoadDataFromNetwork)
        }

        buttonNewPosts.setOnClickListener {
            buttonNewPosts.isVisible = false
            recyclerPosts.smoothScrollToPosition(0)
        }
    }

    override fun render(state: State) {
        val adapterItemsCount = postsViewModel.getPostsAdapter().itemCount

        if (adapterItemsCount == 0 && state.isLoading) {
            shimmerPosts.startShimmer()
            shimmerPosts.isVisible = true
        } else {
            shimmerPosts.stopShimmer()
            shimmerPosts.isVisible = false
        }

        swipeRefreshPosts.isRefreshing = state.isLoading

        recyclerPosts.isVisible = state.items.isNotEmpty() || adapterItemsCount > 0
        emptyStateContainer.isVisible = !recyclerPosts.isVisible && !state.isLoading

        if (state.items.isNotEmpty())
            postsViewModel.getPostsAdapter().submitList(state.items)

        buttonNewPosts.isVisible = adapterItemsCount > 0 && state.items.size > adapterItemsCount
        if (buttonNewPosts.isVisible) {
            buttonNewPosts.animate()
                .setDuration(1000)
                .alpha(0f)
                .startDelay = 4000
        }

        state.error?.let {
            showMessage(it.message.toString())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getPresenter().input.accept(Action.LoadDataFromDB)
    }

    override fun handleUiEffect(uiEffect: UiEffect) {
        when (uiEffect) {
            is UiEffect.ActionError -> {
                showMessage(uiEffect.error.message.toString())
            }
            is UiEffect.ShowPostDetails -> {
                val intentDetails = Intent(activity, PostDetailsActivity::class.java)
                intentDetails.putExtra(
                    PostDetailsActivity.POST_DATA,
                    uiEffect.postWithOwner.post.id
                )
                activity?.startActivity(intentDetails)
            }
            is UiEffect.ShowPostImages -> {
                val intentImages = Intent(activity, ImagesActivity::class.java)
                intentImages.putExtra(ImagesActivity.POST_DATA, uiEffect.postWithOwner.post.id)
                activity?.startActivity(intentImages)
            }
            else -> {
            }
        }
    }
}