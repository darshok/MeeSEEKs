package com.glootie.meeseeks.core.di

import com.glootie.meeseeks.data.local.MeeseeksDatabase
import com.glootie.meeseeks.data.remote.ApiService
import com.glootie.meeseeks.data.repository.CharacterRepository
import com.glootie.meeseeks.data.repository.LocationRepository
import com.glootie.meeseeks.data.repository.impl.CharacterRepositoryImpl
import com.glootie.meeseeks.data.repository.impl.LocationRepositoryImpl
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
    fun providerCharacterRepositoryImpl(apiService: ApiService, database: MeeseeksDatabase) : CharacterRepository {
        return CharacterRepositoryImpl(apiService, database)
    }

    @Provides
    @Singleton
    fun providerLocationRepositoryImpl(apiService: ApiService, database: MeeseeksDatabase) : LocationRepository {
        return LocationRepositoryImpl(apiService, database)
    }
}
