package com.example.filodiscuss.cors.di

import com.example.filodiscuss.features.auth.data.repository.AuthRepositoryImpl
import com.example.filodiscuss.features.auth.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsAuthRepository(impl: AuthRepositoryImpl) : AuthRepository
}