package com.google.android.journal.data.model

import android.os.Parcelable
import androidx.annotation.Nullable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "posts")
@Parcelize
data class Post(
    val categories: List<String>?,
    @PrimaryKey
    var _id: String,
    val title: String?,
    @Nullable
    var favorite_id: String?,
    val pubDate: String?,
    val link: String?,
    val guid: String?,
    val author: String?,
    val thumbnail: String?,
    val description: String?,
    val content: String?,
    val isFavorite: Boolean? = (favorite_id != null)
) : Parcelable