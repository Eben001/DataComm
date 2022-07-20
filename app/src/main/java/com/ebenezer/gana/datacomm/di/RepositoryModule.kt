package com.ebenezer.gana.datacomm.di

import com.ebenezer.gana.datacomm.data.repository.PayTevRepository
import com.ebenezer.gana.datacomm.data.repository.PayTevRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRemoteApi(repositoryImpl: PayTevRepositoryImpl): PayTevRepository
}