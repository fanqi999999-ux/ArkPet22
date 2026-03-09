package com.example.arkpet2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "feed_records")
data class FeedRecordEntity(
    @PrimaryKey val id: String,
    val petId: String,
    val food: String,
    val amount: String,
    val note: String,
    val timeMillis: Long
)