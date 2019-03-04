package com.google.android.journal.injection

import com.google.android.journal.data.remote.ApiService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule{



    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return ApiService.create()
    }
}