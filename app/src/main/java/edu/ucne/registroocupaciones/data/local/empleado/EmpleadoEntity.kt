package edu.ucne.registroocupaciones.data.local.empleado

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import edu.ucne.registroocupaciones.data.local.ocupacion.OcupacionEntity
import java.time.LocalDate

@Entity(tableName = "empleados",
    foreignKeys = [
        ForeignKey(
            entity = OcupacionEntity::class,
            parentColumns = ["ocupacionId"],
            childColumns = ["ocupacionId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ocupacionId")]
)
data class EmpleadoEntity(
    @PrimaryKey(autoGenerate = true)
    val empleadoId: Int,
    val ocupacionId: Int,
    val fechaIngreso: LocalDate,
    val nombres: String,
    val sexo: String,
    val sueldo: Double,
    val frecuenciaPago: FrecuenciaPago
)