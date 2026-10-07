package com.glootie.meeseeks.core.di

import com.glootie.meeseeks.data.local.MeeseeksDatabase
import com.glootie.meeseeks.data.local.dao.CharacterDao
import com.glootie.meeseeks.data.local.dao.LocationDao
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
    fun providerCharacterRepositoryImpl(apiService: ApiService, characterDao: CharacterDao, database: MeeseeksDatabase) : CharacterRepository {
        return CharacterRepositoryImpl(apiService, characterDao, database)
    }

    @Provides
    @Singleton
    fun providerLocationRepositoryImpl(apiService: ApiService, locationDao: LocationDao) : LocationRepository {
        return LocationRepositoryImpl(apiService, locationDao)
    }
}
