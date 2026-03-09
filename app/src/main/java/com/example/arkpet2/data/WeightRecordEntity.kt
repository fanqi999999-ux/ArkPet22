package com.example.arkpet2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_records")
data class WeightRecordEntity(
    @PrimaryKey val id: String,
    val petId: String,
    val value: Double,
    val unit: String,
    val timeMillis: Long
)