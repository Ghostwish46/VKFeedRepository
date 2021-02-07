package dev.ghost.homework_2.presentation

import dev.ghost.homework_2.model.entities.Photo
import dev.ghost.homework_2.model.entities.PostWithOwner
import dev.ghost.homework_2.model.models.AdapterItem

sealed class Action {
    object LoadDataFromNetwork : Action()

    object LoadDataFromDB : Action()

    object DataRefreshed : Action()

    data class ErrorLoadData(val error: Throwable) : Action()

    data class AdapterDataLoaded(val items: List<AdapterItem>) : Action()
}