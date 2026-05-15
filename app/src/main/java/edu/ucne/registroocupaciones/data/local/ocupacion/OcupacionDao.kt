package edu.ucne.registroocupaciones.data.local.ocupacion

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import edu.ucne.registroocupaciones.data.local.ocupacion.OcupacionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OcupacionDao {
    @Upsert
    suspend fun upsert(entity: OcupacionEntity)

    @Query("SELECT * FROM Ocupaciones ORDER BY ocupacionId DESC")
    fun listar(): Flow<List<OcupacionEntity>>

    @Query("SELECT * FROM Ocupaciones WHERE ocupacionId = :id")
    suspend fun buscar(id: Int): OcupacionEntity?

    @Query("DELETE FROM Ocupaciones WHERE ocupacionId = :id")
    suspend fun eliminar(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM Ocupaciones WHERE ocupacionId = :id)")
    suspend fun existe(id: Int): Boolean
}