package edu.ucne.registroocupaciones.data.database

import androidx.room.TypeConverter
import edu.ucne.registroocupaciones.data.local.empleado.FrecuenciaPago
import edu.ucne.registroocupaciones.data.local.horaextra.TipoHoraExtra
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun localDateFromString(value: String?): LocalDate?{
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun localDateToString(date: LocalDate?): String?
    {
        return date?.toString()
    }

    @TypeConverter
    fun frecuenciaPagoFromString(value: String): FrecuenciaPago {
        return FrecuenciaPago.valueOf(value)
    }

    @TypeConverter
    fun frecuenciaPagoToString(value: FrecuenciaPago): String{
        return value.name
    }

    @TypeConverter
    fun tipoHoraExtraFromString(value: String): TipoHoraExtra {
        return TipoHoraExtra.valueOf(value)
    }

    @TypeConverter
    fun tipoHoraExtraToString(value: TipoHoraExtra): String{
        return value.name
    }
}