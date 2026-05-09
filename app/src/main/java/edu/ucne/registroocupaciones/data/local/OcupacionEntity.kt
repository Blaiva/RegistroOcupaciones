package edu.ucne.registroocupaciones.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ocupaciones")
data class OcupacionEntity(
    @PrimaryKey(autoGenerate = true)
    val ocupacionId: Int,
    val descripcion: String,
    val sueldo: Double
)