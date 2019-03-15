package com.google.android.journal.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.android.journal.data.model.Favorite
import com.google.android.journal.data.model.Post

@Dao
interface JournalDao {

    @Query("SELECT * FROM posts ORDER BY pubDate ASC")
    fun getPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM posts WHERE title =:articleTitle")
    fun findByTitle(articleTitle: String): LiveData<Post>

    @Query("SELECT * FROM posts ORDER BY pubDate DESC")
    fun getPostsDesc(): LiveData<List<Post>>

    @Query("SELECT * FROM favorites ORDER BY pubDate ASC")
    fun getFavorites(): LiveData<List<Favorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<Post>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(post: Favorite)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllFavorites(posts: List<Favorite>?)

    @Query("DELETE FROM favorites WHERE id NOT IN(:listOfFavorites)")
    fun deleteOldData(listOfFavorites: List<String>)
}
