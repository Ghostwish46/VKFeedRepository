package dev.ghost.homework_2.presentation.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import dev.ghost.homework_2.R
import dev.ghost.homework_2.model.network.ApiVariables

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        VK.login(this, arrayListOf(VKScope.WALL, VKScope.FRIENDS, VKScope.OFFLINE))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                // User passed authorization
                ApiVariables.apiToken = token.accessToken
                ApiVariables.USER_ID = VK.getUserId()
                Toast.makeText(this@LoginActivity, "Approved", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@LoginActivity, MenuActivity::class.java)
                startActivity(intent)
            }

            override fun onLoginFailed(errorCode: Int) {
                // User didn't pass authorization
                Toast.makeText(this@LoginActivity, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}