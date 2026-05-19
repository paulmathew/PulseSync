package com.paulmathew.pulsesync.di

import com.paulmathew.pulsesync.sync.runtime.FakeSyncOrchestrator
import com.paulmathew.pulsesync.sync.runtime.SyncOrchestrator
import com.paulmathew.pulsesync.sync.runtime.SyncRuntimeSeed
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RuntimeModule{

    @Provides
    @Singleton
    fun provideSyncOrchestrator(): SyncOrchestrator{
        return FakeSyncOrchestrator().also{orchestrator ->
            SyncRuntimeSeed.seed(orchestrator)
        }
    }
}