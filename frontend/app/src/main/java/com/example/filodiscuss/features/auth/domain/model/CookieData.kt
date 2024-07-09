package com.example.filodiscuss.features.auth.domain.model

import kotlinx.serialization.Serializable
import okhttp3.Cookie

@Serializable
data class CookieData(
    val name: String,
    val value: String,
    val expiresAt: Long,
    val domain: String,
    val path: String?,
    val secure: Boolean,
    val httpOnly: Boolean,
    val hostOnly: Boolean,
    val persistent: Boolean,
)

fun Cookie.toCookieData(): CookieData {
    return CookieData(
        name = name,
        value = value,
        expiresAt = expiresAt,
        domain = domain,
        path = path,
        secure = secure,
        httpOnly = httpOnly,
        hostOnly = hostOnly,
        persistent = persistent
    )
}

fun CookieData.toCookie(): Cookie {
    return Cookie.Builder()
        .name(name)
        .value(value)
        .expiresAt(expiresAt)
        .domain(domain)
        .path(path ?: "/")
        .apply {
            if (secure) secure()
            if (httpOnly) httpOnly()
            if (hostOnly) hostOnlyDomain(domain)
        }
        .build()
}
