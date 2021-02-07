package dev.ghost.homework_2.presentation.base.mvp

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import dev.ghost.homework_2.presentation.base.mvp.presenter.Presenter

abstract class MvpFragment<View, P : Presenter<View>> : Fragment(),
    MvpViewCallback<View, P> {

    private val mvpHelper: MvpHelper<View, P> by lazy { MvpHelper(this) }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mvpHelper.create()
    }

    override fun onDestroyView() {
        val isFinishing = isRemoving || requireActivity().isFinishing
        mvpHelper.destroy(isFinishing)
        super.onDestroyView()
    }

    fun showMessage(message: String) =
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
}