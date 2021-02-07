package dev.ghost.homework_2

import android.app.Application
import android.widget.Toast
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKTokenExpiredHandler
import dev.ghost.homework_2.di.AppComponent
import dev.ghost.homework_2.di.AppModule
import dev.ghost.homework_2.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    private val tokenTracker = object : VKTokenExpiredHandler {
        override fun onTokenExpired() {
            // token expired
            Toast.makeText(this@App, "token expired", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
        appComponent.inject(this)
        VK.addTokenExpiredHandler(tokenTracker)

    }

}