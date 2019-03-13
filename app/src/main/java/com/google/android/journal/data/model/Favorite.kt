package com.google.android.journal.data.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "favorites")
@Parcelize
data class Favorite(

    @PrimaryKey
    @SerializedName("_id")
    val id: String,
    @Embedded
    val article: Post

) : Parcelable