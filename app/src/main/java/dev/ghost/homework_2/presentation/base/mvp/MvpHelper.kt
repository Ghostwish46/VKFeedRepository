package dev.ghost.homework_2.presentation.base.mvp

import dev.ghost.homework_2.presentation.base.mvp.presenter.Presenter

class MvpHelper<View, P : Presenter<View>>(
    private val callback: MvpViewCallback<View, P>
) {

    private lateinit var presenter: Presenter<View>

    fun create() {
        presenter = callback.getPresenter()
        presenter.attachView(callback.getMvpView())
    }

    fun destroy(isFinishing: Boolean) {
        presenter.detachView(isFinishing)
    }
}