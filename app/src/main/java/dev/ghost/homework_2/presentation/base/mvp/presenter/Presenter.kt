package dev.ghost.homework_2.presentation.base.mvp.presenter

interface Presenter<View> {
    fun attachView(view: View)

    fun detachView(isFinishing: Boolean)
}