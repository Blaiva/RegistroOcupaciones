package edu.ucne.registroocupaciones.data.local.horaextra

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface HoraExtraDao {
    @Upsert
    suspend fun upsert(entity: HoraExtraEntity)

    @Query("SELECT * FROM horas_extras ORDER BY horaExtraId DESC")
    fun listar(): Flow<List<HoraExtraEntity>>

    @Query("SELECT * FROM horas_extras WHERE horaExtraId = :id")
    suspend fun buscar(id: Int): HoraExtraEntity?

    @Query("DELETE FROM empleados WHERE empleadoId = :id")
    suspend fun eliminar(id: Int)

    @Query("SELECT 1 FROM empleados WHERE empleadoId = :id")
    suspend fun existe(id: Int): Boolean
}