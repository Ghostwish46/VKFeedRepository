package dev.ghost.homework_2.presentation.post

import dev.ghost.homework_2.presentation.State
import dev.ghost.homework_2.presentation.UiEffect

interface AdapterView {
    fun render(state: State)

    fun handleUiEffect(uiEffect: UiEffect)
}