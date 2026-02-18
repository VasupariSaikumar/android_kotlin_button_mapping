package com.pragament.buttonmapper.data.repository

import com.pragament.buttonmapper.data.db.ButtonMappingDao
import com.pragament.buttonmapper.data.model.ButtonMapping
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ButtonMappingRepository @Inject constructor(
    private val dao: ButtonMappingDao
) {
    fun getAllMappings(): Flow<List<ButtonMapping>> = dao.getAllMappings()

    fun getMappingByIdFlow(id: Long): Flow<ButtonMapping?> = dao.getMappingByIdFlow(id)

    fun getMappingByKeyCodeFlow(keyCode: Int): Flow<ButtonMapping?> = dao.getMappingByKeyCodeFlow(keyCode)

    suspend fun getMappingByKeyCode(keyCode: Int): ButtonMapping? = dao.getMappingByKeyCode(keyCode)

    suspend fun getMappingById(id: Long): ButtonMapping? = dao.getMappingById(id)

    suspend fun insertMapping(mapping: ButtonMapping): Long = dao.insertMapping(mapping)

    suspend fun updateMapping(mapping: ButtonMapping) = dao.updateMapping(mapping)

    suspend fun deleteMapping(mapping: ButtonMapping) = dao.deleteMapping(mapping)

    suspend fun deleteMappingById(id: Long) = dao.deleteMappingById(id)

    suspend fun getCount(): Int = dao.getCount()
}
