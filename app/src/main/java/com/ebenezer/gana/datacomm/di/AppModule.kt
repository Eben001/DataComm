package com.ebenezer.gana.datacomm.di

import android.content.Context
import com.ebenezer.gana.datacomm.prefsStore.UserPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun dataStoreManager(@ApplicationContext appContext:Context):UserPreference =
        UserPreference(appContext)
}