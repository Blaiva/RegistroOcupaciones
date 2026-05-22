package edu.ucne.registroocupaciones.data.local.horaextra

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "horas_extras")
data class HoraExtraEntity(
    @PrimaryKey(autoGenerate = true)
    val horaExtraid: Int,
    val ocupacionId: Int,
    val fecha: LocalDate,
    val cantidadHoras: Int,
    val tipo: TipoHoraExtra,
    val recargo: Double
)
