package com.kvad.totalizator.di

import android.content.Context
import com.kvad.totalizator.data.EventRepository
import com.kvad.totalizator.data.api.EventMockService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Singleton
    @Provides
    fun context(): Context {
        return context
    }

    @Singleton
    @Provides
    fun provideEventRepository(): EventRepository {
        return EventRepository(EventMockService())
    }
}