package com.example.arkpet2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey val id: String,
    val name: String,
    val species: String,
    val breed: String,
    val sex: String,
    val birthday: String,
    val note: String,
    val avatarUri: String
)