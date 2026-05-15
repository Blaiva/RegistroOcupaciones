package edu.ucne.registroocupaciones.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.registroocupaciones.data.repository.empleado.EmpleadoRepositoryImpl
import edu.ucne.registroocupaciones.data.repository.ocupacion.OcupacionRepositoryImpl
import edu.ucne.registroocupaciones.domain.repository.empleado.EmpleadoRepository
import edu.ucne.registroocupaciones.domain.repository.ocupacion.OcupacionRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindOcupacionRepository(
        impl: OcupacionRepositoryImpl
    ): OcupacionRepository

    @Binds
    @Singleton
    abstract fun bindEmpleadoRepository(
        impl: EmpleadoRepositoryImpl
    ): EmpleadoRepository
}