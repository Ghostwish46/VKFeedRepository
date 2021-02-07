package dev.ghost.homework_2.presentation.base.mvp

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dev.ghost.homework_2.presentation.base.mvp.presenter.Presenter

abstract class MvpActivity<View, P : Presenter<View>> : AppCompatActivity(),
    MvpViewCallback<View, P> {

    private val mvpHelper: MvpHelper<View, P> by lazy { MvpHelper(this) }

    override fun onStart() {
        super.onStart()
        mvpHelper.create()
    }

    override fun onDestroy() {
        mvpHelper.destroy(isFinishing)
        super.onDestroy()
    }

    fun showMessage(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}