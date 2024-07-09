package com.example.filodiscuss.features.auth.data.network.di

import com.example.filodiscuss.features.auth.data.local.PreferencesLocal
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import javax.inject.Inject

class UserCookieJar @Inject constructor(private val preferencesLocal: PreferencesLocal) : CookieJar {
    private val cookieStore = mutableMapOf<String, List<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies
        cookies.forEach { cookie ->
            preferencesLocal.saveCookie(url.host, cookie)
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return preferencesLocal.getCookie(url.host)?.let { listOf(it) } ?: emptyList()
    }
}
