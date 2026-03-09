package com.example.arkpet2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medical_records")
data class MedicalRecordEntity(
    @PrimaryKey val id: String,
    val petId: String,
    val hospital: String,
    val diagnosis: String,
    val treatment: String,
    val note: String,
    val timeMillis: Long
)