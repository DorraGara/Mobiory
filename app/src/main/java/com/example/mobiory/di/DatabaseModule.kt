package com.example.mobiory.di

import android.content.Context
import com.example.mobiory.data.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    fun provideUserDatabase(@ApplicationContext context: Context) : AppDatabase =
        AppDatabase.getDatabase(context)

    @Provides
    fun provideUserDao(appDatabase: AppDatabase) = appDatabase.eventDao()
}