package dev.ghost.homework_2.presentation.base.mvp.presenter

import androidx.annotation.CallSuper
import dev.ghost.homework_2.utils.reflection.ReflectionUtils

abstract class BasePresenter<View> protected constructor(
    viewClass: Class<View>
) : Presenter<View> {

    private val stubView: View = ReflectionUtils.createStub(viewClass)
    private var realView: View? = null

    val view: View
        get() = realView ?: stubView


    @CallSuper
    override fun attachView(view: View) {
        this.realView = view
    }

    @CallSuper
    override fun detachView(isFinishing: Boolean) {
        realView = null
    }

    fun hasView(): Boolean {
        return view != null
    }
}