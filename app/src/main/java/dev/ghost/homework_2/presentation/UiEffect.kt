package dev.ghost.homework_2.presentation

import android.content.Intent
import dev.ghost.homework_2.model.entities.PostWithOwner

sealed class UiEffect {
    class ActionError(val error: Throwable) : UiEffect()
    class ShowMessage(val message: String) : UiEffect()
    class ShowPostDetails(val postWithOwner: PostWithOwner) : UiEffect()
    class ShowPostImages(val postWithOwner: PostWithOwner) : UiEffect()
    class CreateIntent(val intent: Intent) : UiEffect()
    object ScrollToEnd : UiEffect()
    object ObjectSent : UiEffect()
    object DisabledComments : UiEffect()
    object DownloadImage : UiEffect()
    object ShareImage : UiEffect()
}