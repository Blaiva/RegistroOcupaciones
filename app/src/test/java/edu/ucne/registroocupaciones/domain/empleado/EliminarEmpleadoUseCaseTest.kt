package edu.ucne.registroocupaciones.domain.empleado

import edu.ucne.registroocupaciones.domain.repository.empleado.EmpleadoRepository
import edu.ucne.registroocupaciones.domain.usecase.empleado.EliminarEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.usecase.ocupacion.EliminarOcupacionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class EliminarEmpleadoUseCaseTest {
    private lateinit var repository: EmpleadoRepository
    private lateinit var useCase: EliminarEmpleadoUseCase

    @Before
    fun setup(){
        repository = mockk(relaxed = true)
        useCase = EliminarEmpleadoUseCase(repository)
    }

    @Test
    fun `calls repository delete with id`() = runTest {
        coEvery { repository.delete(5) } just runs

        useCase(5)

        coVerify { repository.delete(5) }
    }
}