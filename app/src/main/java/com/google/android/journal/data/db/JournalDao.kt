package com.google.android.journal.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.android.journal.data.model.Post

@Dao
interface JournalDao {

    @Query("SELECT * FROM posts")
    fun getPosts(): LiveData<List<Post>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<Post>?)
}