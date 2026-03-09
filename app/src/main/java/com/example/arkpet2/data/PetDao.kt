package com.example.arkpet2.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PetDao {

    @Query("SELECT * FROM pets")
    suspend fun getAllPets(): List<PetEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity)

    @Query("DELETE FROM pets WHERE id = :petId")
    suspend fun deletePetById(petId: String)

    @Query("SELECT * FROM weight_records WHERE petId = :petId ORDER BY timeMillis DESC")
    suspend fun getWeightRecordsByPetId(petId: String): List<WeightRecordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightRecord(record: WeightRecordEntity)

    @Query("DELETE FROM weight_records WHERE id = :recordId")
    suspend fun deleteWeightRecordById(recordId: String)

    @Query("SELECT * FROM feed_records WHERE petId = :petId ORDER BY timeMillis DESC")
    suspend fun getFeedRecordsByPetId(petId: String): List<FeedRecordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeedRecord(record: FeedRecordEntity)

    @Query("DELETE FROM feed_records WHERE id = :recordId")
    suspend fun deleteFeedRecordById(recordId: String)

    @Query("SELECT * FROM medical_records WHERE petId = :petId ORDER BY timeMillis DESC")
    suspend fun getMedicalRecordsByPetId(petId: String): List<MedicalRecordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicalRecord(record: MedicalRecordEntity)

    @Query("DELETE FROM medical_records WHERE id = :recordId")
    suspend fun deleteMedicalRecordById(recordId: String)

    @Query("SELECT * FROM medication_records WHERE petId = :petId ORDER BY timeMillis DESC")
    suspend fun getMedicationRecordsByPetId(petId: String): List<MedicationRecordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMedicationRecord(record: MedicationRecordEntity)

    @Query("DELETE FROM medication_records WHERE id = :recordId")
    suspend fun deleteMedicationRecordById(recordId: String)
}