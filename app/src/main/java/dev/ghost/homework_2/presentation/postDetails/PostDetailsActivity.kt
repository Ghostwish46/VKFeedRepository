package dev.ghost.homework_2.presentation.postDetails

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import dev.ghost.homework_2.R
import dev.ghost.homework_2.presentation.Action
import dev.ghost.homework_2.presentation.State
import dev.ghost.homework_2.presentation.UiEffect
import dev.ghost.homework_2.presentation.base.mvp.MvpActivity
import dev.ghost.homework_2.presentation.post.AdapterView
import dev.ghost.homework_2.presentation.images.ImagesActivity
import kotlinx.android.synthetic.main.activity_post_details.*

class PostDetailsActivity : MvpActivity<AdapterView, PostDetailsPresenter>(), AdapterView {
    companion object {
        const val POST_DATA = "postData"
    }

    private lateinit var postDetailsViewModel: PostDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_details)
        postDetailsViewModel = ViewModelProvider(this).get(PostDetailsViewModel::class.java)

        val postId = intent.extras?.getInt(POST_DATA)
        postId?.let {
            postDetailsViewModel.setPostId(postId)
        }

        recyclerPostDetails.adapter = postDetailsViewModel.getPostsAdapter()

        swipeRefreshPostDetails.setOnRefreshListener {
            postDetailsViewModel.getPostDetailsPresenter().input.accept(Action.LoadDataFromNetwork)
        }

        addCommentSend.setOnClickListener {
            changeElementsVisibility(true)
            postDetailsViewModel.sendComment(addCommentText.text.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        getPresenter().input.accept(Action.LoadDataFromDB)
    }

    override fun getPresenter(): PostDetailsPresenter {
        return postDetailsViewModel.getPostDetailsPresenter()
    }

    override fun getMvpView(): AdapterView = this

    override fun render(state: State) {
        swipeRefreshPostDetails.isRefreshing = state.isLoading

        if (state.items.isNotEmpty()) {
            postDetailsViewModel.getPostsAdapter().submitList(state.items)
        }

        state.error?.let {
            showMessage(it.message.toString())
        }
    }

    override fun handleUiEffect(uiEffect: UiEffect) {
        when (uiEffect) {
            is UiEffect.ActionError -> {
                showMessage(uiEffect.error.message.toString())
                changeElementsVisibility(false)
            }
            is UiEffect.ShowPostImages -> {
                val intentImages = Intent(this, ImagesActivity::class.java)
                intentImages.putExtra(ImagesActivity.POST_DATA, uiEffect.postWithOwner)
                intentImages.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intentImages)
            }
            is UiEffect.ScrollToEnd -> {
                val currentCount = postDetailsViewModel.getPostsAdapter().itemCount - 1
                if (currentCount > 0)
                    recyclerPostDetails
                        .scrollToPosition(currentCount)
            }
            is UiEffect.DisabledComments -> {
                addCommentText.isVisible = false
                addCommentSend.isVisible = false
            }
            is UiEffect.ObjectSent -> {
                addCommentText.setText("")
                changeElementsVisibility(false)
            }
            else -> {
            }
        }
    }

    private fun changeElementsVisibility(isSending: Boolean = false) {
        if (isSending) {
            addCommentProgress.isVisible = true
            addCommentSend.isVisible = false
        } else {
            addCommentProgress.isVisible = false
            addCommentSend.isVisible = true
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}