package com.example.filodiscuss.cors.di

import com.example.filodiscuss.features.auth.data.repository.AuthRepositoryImpl
import com.example.filodiscuss.features.auth.domain.repository.AuthRepository
import com.example.filodiscuss.features.home.data.repository.PostRepositoryImpl
import com.example.filodiscuss.features.home.domain.repository.PostRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsAuthRepository(impl: AuthRepositoryImpl) : AuthRepository

    @Binds
    abstract fun bindsPostRepository(impl: PostRepositoryImpl): PostRepository

}