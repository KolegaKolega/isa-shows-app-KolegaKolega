package com.example.shows_kolegakolega.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shows_kolegakolega.model.User

@Entity(tableName = "review")
data class ReviewEntity(
    @PrimaryKey @ColumnInfo(name = "review_id") val id: String,
    @ColumnInfo(name = "comment") val comment: String?,
    @ColumnInfo(name = "rating") val rating: Int,
    @ColumnInfo(name = "show_id") val showId: Int,
    @Embedded val user: User
)
