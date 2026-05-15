package edu.ucne.registroocupaciones.data.local.empleado

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "Empleados")
data class EmpleadoEntity(
    @PrimaryKey(autoGenerate = true)
    val empleadoId: Int,
    val fechaIngreso: LocalDate,
    val nombres: String,
    val sexo: String,
    val sueldo: Double
)