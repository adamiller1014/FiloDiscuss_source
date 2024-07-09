package com.example.filodiscuss.features.auth.data.network.di

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.example.filodiscuss.BuildConfig
import com.example.filodiscuss.features.auth.data.local.PreferencesLocal
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

private const val BASE_URL = "http://10.0.2.2:4000/graphql"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providePreferencesLocal(@ApplicationContext context: Context): PreferencesLocal {
        return PreferencesLocal(context)
    }

    @Provides
    @Singleton
    fun provideCookieJar(preferencesLocal: PreferencesLocal): UserCookieJar {
        return UserCookieJar(preferencesLocal)
    }

    @Provides
    @Singleton
    fun okhttpClient(cookieJar : UserCookieJar): OkHttpClient {
        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideApollo(client: OkHttpClient): ApolloClient {
        return ApolloClient.Builder()
            .serverUrl(BASE_URL)
            .okHttpClient(client)
            .build()
    }
}