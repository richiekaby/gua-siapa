package net.larntech.guasiapa.share_pref

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import net.larntech.guasiapa.App
import net.larntech.guasiapa.model.login.LoginResponse

object  SharePref {

    private val USER_LOGIN = "USER_LOGIN"
    private val gson = Gson()

    private fun getPreferences(): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(
            App.getInstance()!!.applicationContext
        )
    }

    private fun getString(preferenceKey: String?, preferenceDefaultValue: String?): String? {
        return getPreferences().getString(preferenceKey, preferenceDefaultValue)
    }

    private fun putString(preferenceKey: String?, preferenceValue: String?) {
        getPreferences().edit().putString(preferenceKey, preferenceValue).apply()
    }


    fun clearPrefs() {
        getPreferences().edit().clear().apply()
    }


    fun getLoginDetails(): LoginResponse? {
        return gson.fromJson(
            getString(
                USER_LOGIN,
                null
            ),
            LoginResponse::class.java
        )
    }


    //save LOGIN
    fun saveLoginResponse(loginResponse: LoginResponse) {
        putString(
            USER_LOGIN,
            gson.toJson(loginResponse)
        )
    }


}