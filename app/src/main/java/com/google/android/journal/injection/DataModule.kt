package com.google.android.journal.injection

import android.app.Application
import androidx.room.Room
import com.google.android.journal.data.db.JournalDao
import com.google.android.journal.data.db.JournalDb
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {



    @Provides
    @Singleton
    internal fun provideDb(app: Application): JournalDb {
        return Room.databaseBuilder(app, JournalDb::class.java, "journal.db").fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    internal fun provideFeedDao(db: JournalDb): JournalDao {
        return db.journalDao()
    }

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return Gson()
    }

}