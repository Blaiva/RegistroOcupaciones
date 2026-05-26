package edu.ucne.registroocupaciones.data.local.ocupacion

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ocupaciones")
data class OcupacionEntity(
    @PrimaryKey(autoGenerate = true)
    val ocupacionId: Int,
    val descripcion: String,
    val esPuestoDireccion: Boolean
)