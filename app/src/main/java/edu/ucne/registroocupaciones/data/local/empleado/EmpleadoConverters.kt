package edu.ucne.registroocupaciones.data.local.empleado

import androidx.room.TypeConverter
import java.time.LocalDate

class EmpleadoConverters {
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
    fun frecuenciaPagoFromString(value: String): FrecuenciaPago{
        return FrecuenciaPago.valueOf(value)
    }

    @TypeConverter
    fun frecuenciaPagoToString(value: FrecuenciaPago): String{
        return value.name
    }
}