package com.glootie.meeseeks.core.di

import com.glootie.meeseeks.data.remote.ApiService
import com.glootie.meeseeks.data.repository.CharacterRepository
import com.glootie.meeseeks.data.repository.impl.CharacterRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providerCharacterRepositoryImpl(apiService: ApiService) : CharacterRepository {
        return CharacterRepositoryImpl(apiService)
    }
}