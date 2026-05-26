package edu.ucne.registroocupaciones.data.local.horaextra

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import edu.ucne.registroocupaciones.data.local.empleado.EmpleadoEntity
import java.time.LocalDate

@Entity(
    tableName = "horas_extras",
    foreignKeys = [
        ForeignKey(
            entity = EmpleadoEntity::class,
            parentColumns = ["empleadoId"],
            childColumns = ["empleadoId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("empleadoId")]
)
data class HoraExtraEntity(
    @PrimaryKey(autoGenerate = true)
    val horaExtraId: Int,
    val empleadoId: Int,
    val fecha: LocalDate,
    val cantidadHoras: Int,
    val tipo: TipoHoraExtra,
    val recargo: Double
)
