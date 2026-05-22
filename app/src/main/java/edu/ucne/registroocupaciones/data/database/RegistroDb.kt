package edu.ucne.registroocupaciones.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.ucne.registroocupaciones.data.local.empleado.EmpleadoConverters
import edu.ucne.registroocupaciones.data.local.empleado.EmpleadoDao
import edu.ucne.registroocupaciones.data.local.empleado.EmpleadoEntity
import edu.ucne.registroocupaciones.data.local.horaextra.HoraExtraConverters
import edu.ucne.registroocupaciones.data.local.horaextra.HoraExtraDao
import edu.ucne.registroocupaciones.data.local.horaextra.HoraExtraEntity
import edu.ucne.registroocupaciones.data.local.ocupacion.OcupacionDao
import edu.ucne.registroocupaciones.data.local.ocupacion.OcupacionEntity

@Database(
    entities = [OcupacionEntity::class, EmpleadoEntity::class, HoraExtraEntity::class],
    version = 3
)
@TypeConverters(EmpleadoConverters::class, HoraExtraConverters::class)
abstract class RegistroDb: RoomDatabase() {
    abstract fun ocupacionDao(): OcupacionDao
    abstract fun empleadoDao(): EmpleadoDao
    abstract fun horaExtraDao(): HoraExtraDao
}