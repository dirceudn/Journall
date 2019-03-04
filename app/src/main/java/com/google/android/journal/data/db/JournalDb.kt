package com.google.android.journal.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.google.android.journal.data.model.Converters
import com.google.android.journal.data.model.Post

@Database(entities = arrayOf(Post::class), version = 1)
@TypeConverters(Converters::class)
abstract class JournalDb : RoomDatabase() {
    abstract fun journalDao(): JournalDao

}