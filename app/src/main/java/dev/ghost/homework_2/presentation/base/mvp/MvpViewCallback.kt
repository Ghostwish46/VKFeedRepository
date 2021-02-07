package dev.ghost.homework_2.presentation.base.mvp

import dev.ghost.homework_2.presentation.base.mvp.presenter.Presenter

interface MvpViewCallback<View, P : Presenter<View>> {

    fun getPresenter(): P

    fun getMvpView(): View
}