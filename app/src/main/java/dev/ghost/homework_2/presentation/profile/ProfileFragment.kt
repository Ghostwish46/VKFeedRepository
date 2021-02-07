package dev.ghost.homework_2.presentation.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import dev.ghost.homework_2.R
import dev.ghost.homework_2.model.entities.Profile
import dev.ghost.homework_2.presentation.Action
import dev.ghost.homework_2.presentation.State
import dev.ghost.homework_2.presentation.UiEffect
import dev.ghost.homework_2.presentation.base.mvp.MvpFragment
import dev.ghost.homework_2.presentation.post.AdapterView
import dev.ghost.homework_2.presentation.adapters.PostTouchHelperCallback
import dev.ghost.homework_2.presentation.newPost.NewPostActivity
import dev.ghost.homework_2.presentation.images.ImagesActivity
import dev.ghost.homework_2.presentation.postDetails.PostDetailsActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.layout_empty_state.*

class ProfileFragment : MvpFragment<AdapterView, ProfilePresenter>(), AdapterView {

    companion object {
        const val SEND_POST_CODE = 101
    }

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerProfile.adapter = profileViewModel.getPostsAdapter()
        val callback = PostTouchHelperCallback(profileViewModel.getPostsAdapter())
        val postTouchHelperAdapter = ItemTouchHelper(callback)
        postTouchHelperAdapter.attachToRecyclerView(recyclerProfile)

        swipeRefreshProfile.setOnRefreshListener {
            profileViewModel.getProfilePresenter().input.accept(Action.LoadDataFromNetwork)
        }

        newPostContainer.setOnClickListener {
            val intent = Intent(activity, NewPostActivity::class.java)
            startActivityForResult(intent, SEND_POST_CODE)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileViewModel.getProfilePresenter().input.accept(Action.LoadDataFromDB)
    }

    override fun getPresenter(): ProfilePresenter = profileViewModel.getProfilePresenter()

    override fun getMvpView(): AdapterView = this

    override fun render(state: State) {
        val adapterItemsCount = profileViewModel.getPostsAdapter().itemCount

        if (adapterItemsCount == 0 && state.isLoading) {
            shimmerProfile.startShimmer()
            shimmerProfile.isVisible = true
        } else {
            shimmerProfile.stopShimmer()
            shimmerProfile.isVisible = false
        }

        swipeRefreshProfile.isRefreshing = state.isLoading
        recyclerProfile.isVisible = state.items.isNotEmpty() || adapterItemsCount > 0
        emptyStateContainer.isVisible = !recyclerProfile.isVisible && !state.isLoading

        if (state.items.isNotEmpty()) {
            profileViewModel.getPostsAdapter().submitList(state.items)

            val firstItem = state.items.first()
            if (firstItem is Profile)
                activity?.title = firstItem.domain
        }

        state.error?.let {
            showMessage(it.message.toString())
        }
    }

    override fun handleUiEffect(uiEffect: UiEffect) {
        when (uiEffect) {
            is UiEffect.ActionError -> {
                showMessage(uiEffect.error.toString())
            }
            is UiEffect.ShowPostDetails -> {
                val intentDetails = Intent(activity, PostDetailsActivity::class.java)
                intentDetails.putExtra(
                    PostDetailsActivity.POST_DATA,
                    uiEffect.postWithOwner.post.id
                )
                startActivity(intentDetails)
            }
            is UiEffect.ShowPostImages -> {
                val intentImages = Intent(activity, ImagesActivity::class.java)
                intentImages.putExtra(ImagesActivity.POST_DATA, uiEffect.postWithOwner.post.id)
                startActivity(intentImages)
            }
            else -> {
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SEND_POST_CODE && resultCode == RESULT_OK) {
            getPresenter().input.accept(Action.LoadDataFromNetwork)
        }
    }
}