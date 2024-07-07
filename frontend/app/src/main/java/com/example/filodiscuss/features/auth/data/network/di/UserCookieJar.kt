package com.example.filodiscuss.features.auth.data.network.di

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class UserCookieJar : CookieJar {
    private val cookieStore = mutableMapOf<String, List<Cookie>>()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: emptyList()
    }
}
