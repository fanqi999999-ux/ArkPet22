package com.example.arkpet2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication_records")
data class MedicationRecordEntity(
    @PrimaryKey val id: String,
    val petId: String,
    val medicineName: String,
    val dosage: String,
    val frequency: String,
    val days: String,
    val note: String,
    val timeMillis: Long
)