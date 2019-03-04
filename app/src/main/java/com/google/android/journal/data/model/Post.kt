package com.google.android.journal.data.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "posts")
data class Post(
    val categories: List<String>,
    @PrimaryKey
    val _id: String,
    val title: String,
    val pubDate: String,
    val link: String,
    val guid: String,
    val author: String,
    val thumbnail: String,
    val description: String,
    val content: String
)