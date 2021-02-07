package dev.ghost.homework_2.presentation

import android.util.Log
import dev.ghost.homework_2.model.models.AdapterItem

data class State(
    val items: List<AdapterItem> = emptyList(),
    val isEmptyState: Boolean = false,
    val error: Throwable? = null,
    val isLoading: Boolean = false
)

internal fun State.reduce(action: Action): State {
    return when (action) {
        is Action.LoadDataFromDB -> copy(
            isLoading = true,
            isEmptyState = false,
            error = null
        )
        is Action.ErrorLoadData -> copy(
            isLoading = false,
            isEmptyState = false,
            error = action.error
        )
        is Action.LoadDataFromNetwork -> copy(
            isLoading = true,
            isEmptyState = false,
            error = null
        )
        is Action.DataRefreshed -> copy(
            isLoading = false,
            isEmptyState = false,
            error = null
        )
        is Action.AdapterDataLoaded -> copy(
            isLoading = false,
            isEmptyState = items.isEmpty(),
            error = null,
            items = action.items
        )
    }
}