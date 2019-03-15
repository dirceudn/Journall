package com.google.android.journal.injection

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.google.android.journal.JournalApp
import com.google.android.journal.ui.view.AppViewModelFactory
import dagger.Binds
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