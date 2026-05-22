package edu.ucne.registroocupaciones.data.local.empleado

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface EmpleadoDao {
    @Upsert
    suspend fun upsert(entity: EmpleadoEntity)

    @Query("SELECT * FROM empleados ORDER BY empleadoId DESC")
    fun listar(): Flow<List<EmpleadoEntity>>

    @Query("SELECT * FROM empleados WHERE empleadoId = :id")
    suspend fun buscar(id: Int): EmpleadoEntity?

    @Query("DELETE FROM empleados WHERE empleadoId = :id")
    suspend fun eliminar(id: Int)

    @Query("SELECT 1 FROM empleados WHERE empleadoId = :id")
    suspend fun existe(id: Int): Boolean
}