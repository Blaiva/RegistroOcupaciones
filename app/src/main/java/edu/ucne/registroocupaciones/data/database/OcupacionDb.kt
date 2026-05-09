package edu.ucne.registroocupaciones.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.ucne.registroocupaciones.data.local.OcupacionDao
import edu.ucne.registroocupaciones.data.local.OcupacionEntity

@Database(
    entities = [OcupacionEntity::class],
    version = 1
)
abstract class OcupacionDb: RoomDatabase() {
    abstract fun ocupacionDao(): OcupacionDao
}