package edu.ucne.registroocupaciones.data.local.horaextra

import androidx.room.TypeConverter
import java.time.LocalDate

class HoraExtraConverters {
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
    fun tipoHoraExtraFromString(value: String): TipoHoraExtra{
        return TipoHoraExtra.valueOf(value)
    }

    @TypeConverter
    fun tipoHoraExtraToString(value: TipoHoraExtra): String{
        return value.descripcion
    }
}