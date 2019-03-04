package com.google.android.journal.injection

import android.app.Application
import com.google.android.journal.JournalApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule (private val mApplication: Application) {



    @Provides
    @Singleton
    fun provideApplication(): Application {
        return mApplication
    }
}