package edu.ucne.registroocupaciones.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registroocupaciones.data.database.RegistroDb
import edu.ucne.registroocupaciones.data.local.empleado.EmpleadoDao
import edu.ucne.registroocupaciones.data.local.horaextra.HoraExtraDao
import edu.ucne.registroocupaciones.data.local.ocupacion.OcupacionDao
import edu.ucne.registroocupaciones.domain.model.horaextra.HoraExtra
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideRegistroDb(@ApplicationContext context: Context): RegistroDb
    {
        return Room.databaseBuilder(
            context, RegistroDb::class.java, "ocupacion_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideOcupacionDao(database: RegistroDb): OcupacionDao
    {
        return database.ocupacionDao()
    }

    @Provides
    @Singleton
    fun provideEmpleadoDao(database: RegistroDb): EmpleadoDao
    {
        return database.empleadoDao()
    }

    @Provides
    @Singleton
    fun provideHoraExtraDao(database: RegistroDb): HoraExtraDao{
        return database.horaExtraDao()
    }
}