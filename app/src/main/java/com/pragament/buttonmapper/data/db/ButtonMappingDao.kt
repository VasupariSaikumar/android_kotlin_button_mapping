package com.pragament.buttonmapper.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pragament.buttonmapper.data.model.ButtonMapping
import kotlinx.coroutines.flow.Flow

@Dao
interface ButtonMappingDao {

    @Query("SELECT * FROM button_mappings ORDER BY buttonName ASC")
    fun getAllMappings(): Flow<List<ButtonMapping>>

    @Query("SELECT * FROM button_mappings WHERE buttonKeyCode = :keyCode LIMIT 1")
    suspend fun getMappingByKeyCode(keyCode: Int): ButtonMapping?

    @Query("SELECT * FROM button_mappings WHERE buttonKeyCode = :keyCode LIMIT 1")
    fun getMappingByKeyCodeFlow(keyCode: Int): Flow<ButtonMapping?>

    @Query("SELECT * FROM button_mappings WHERE id = :id LIMIT 1")
    suspend fun getMappingById(id: Long): ButtonMapping?

    @Query("SELECT * FROM button_mappings WHERE id = :id LIMIT 1")
    fun getMappingByIdFlow(id: Long): Flow<ButtonMapping?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMapping(mapping: ButtonMapping): Long

    @Update
    suspend fun updateMapping(mapping: ButtonMapping)

    @Delete
    suspend fun deleteMapping(mapping: ButtonMapping)

    @Query("DELETE FROM button_mappings WHERE id = :id")
    suspend fun deleteMappingById(id: Long)

    @Query("SELECT COUNT(*) FROM button_mappings")
    suspend fun getCount(): Int
}
