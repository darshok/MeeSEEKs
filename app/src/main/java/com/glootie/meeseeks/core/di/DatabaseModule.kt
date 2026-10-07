package com.glootie.meeseeks.core.di

import android.content.Context
import androidx.room.Room
import com.glootie.meeseeks.core.DATABASE_NAME
import com.glootie.meeseeks.data.local.MeeseeksDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MeeseeksDatabase {
        return Room.databaseBuilder(
            context,
            MeeseeksDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideCharacterDao(database: MeeseeksDatabase) = database.characterDao()

    @Provides
    fun provideLocationDao(database: MeeseeksDatabase) = database.locationDao()

    @Provides
    fun provideRemoteKeyDao(database: MeeseeksDatabase) = database.remoteKeyDao()
}
