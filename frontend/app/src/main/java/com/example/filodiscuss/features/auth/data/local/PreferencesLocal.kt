package com.example.filodiscuss.features.auth.data.local

import android.content.Context
import android.content.SharedPreferences
import com.example.filodiscuss.features.auth.domain.model.CookieData
import com.example.filodiscuss.features.auth.domain.model.toCookie
import com.example.filodiscuss.features.auth.domain.model.toCookieData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.Cookie
import javax.inject.Inject

class PreferencesLocal @Inject constructor(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("CookiePrefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    fun saveCookie(key: String, cookie: Cookie) {
        val editor = sharedPreferences.edit()
        val cookieData = cookie.toCookieData()
        val cookieJson = json.encodeToString(cookieData)
        editor.putString(key, cookieJson)
        editor.apply()
    }

    fun getCookie(key: String): Cookie? {
        val cookieJson = sharedPreferences.getString(key, null) ?: return null
        return try {
            val cookieData = json.decodeFromString<CookieData>(cookieJson)
            cookieData.toCookie()
        } catch (e: Exception) {
            null
        }
    }

    fun clearCookies() {
        sharedPreferences.edit().clear().apply()
    }
}
