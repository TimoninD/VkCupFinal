package lead.codeoverflow.vkcupfinal.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import com.vk.api.sdk.auth.VKScope
import com.vk.api.sdk.utils.VKUtils
import kotlinx.android.synthetic.main.activity_main.*
import lead.codeoverflow.vkcupfinal.R
import lead.codeoverflow.vkcupfinal.model.Prefs
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val prefs: Prefs by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (prefs.accessToken.isEmpty()) {
            //VK.login(this, arrayListOf(VKScope.WALL, VKScope.FRIENDS))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                prefs.accessToken = token.accessToken
                prefs.uid = token.userId
            }

            override fun onLoginFailed(errorCode: Int) {
                Snackbar.make(root, "", Snackbar.LENGTH_LONG).show()
            }
        }
        if (data == null || !VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}