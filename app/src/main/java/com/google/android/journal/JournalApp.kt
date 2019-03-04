package com.google.android.journal

import android.app.Application
import android.os.StrictMode
import com.google.android.journal.injection.AppComponent
import com.google.android.journal.injection.AppModule
import com.google.android.journal.injection.DaggerAppComponent
import timber.log.Timber


class JournalApp : Application() {


    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())

        }
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent
            private set
    }
}