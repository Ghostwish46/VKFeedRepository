package dev.ghost.homework_2.presentation.newPost

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import dev.ghost.homework_2.R
import dev.ghost.homework_2.presentation.Action
import dev.ghost.homework_2.presentation.State
import dev.ghost.homework_2.presentation.UiEffect
import dev.ghost.homework_2.presentation.base.mvp.MvpActivity
import dev.ghost.homework_2.presentation.post.AdapterView
import kotlinx.android.synthetic.main.activity_new_post.*
import kotlinx.android.synthetic.main.activity_post_details.*

class NewPostActivity : MvpActivity<AdapterView, NewPostPresenter>(), AdapterView {

    private lateinit var newPostViewModel: NewPostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
        title = getString(R.string.title_new_post)

        newPostViewModel = ViewModelProvider(this).get(NewPostViewModel::class.java)

        newPostSend.setOnClickListener {
            changeElementsVisibility(true)
            newPostViewModel.sendPost(newPostText.text.toString())
        }
    }

    private fun changeElementsVisibility(isSending: Boolean = false) {
        if (isSending) {
            newPostProgress.isVisible = true
            newPostSend.visibility = View.INVISIBLE
        } else {
            newPostProgress.isVisible = false
            newPostSend.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        getPresenter().input.accept(Action.LoadDataFromDB)
    }

    override fun render(state: State) {
    }

    override fun handleUiEffect(uiEffect: UiEffect) {
        when (uiEffect) {
            is UiEffect.ActionError -> {
                changeElementsVisibility(false)
                showMessage(uiEffect.error.message.toString())
            }
            is UiEffect.ObjectSent -> {
                showMessage("New post was created.")
                setResult(RESULT_OK)
                finish()
            }
            else -> {
            }
        }
    }

    override fun getPresenter(): NewPostPresenter {
        return newPostViewModel.getPresenter()
    }

    override fun getMvpView(): AdapterView = this
}