package lead.codeoverflow.vkcupfinal.model

import android.content.Context

class Prefs constructor(
    private val context: Context
) {

    private fun getSharedPreferences(prefsName: String) =
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)


    companion object {
        private const val APP_DATA = "app_data"
        private const val ACCESS_TOKEN = "access_token"
        private const val UID = "uid"
    }

    private val appPrefs by lazy { getSharedPreferences(APP_DATA) }

    var accessToken: String
        get() = appPrefs.getString(ACCESS_TOKEN, "").orEmpty()
        set(value) {
            appPrefs.edit().putString(ACCESS_TOKEN, value).apply()
        }

    var uid: Int
        get() = appPrefs.getInt(UID, 0)
        set(value) {
            appPrefs.edit().putInt(UID, value).apply()
        }

}