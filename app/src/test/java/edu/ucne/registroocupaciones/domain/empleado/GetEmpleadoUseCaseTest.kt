package edu.ucne.registroocupaciones.domain.empleado

import edu.ucne.registroocupaciones.domain.model.empleado.Empleado
import edu.ucne.registroocupaciones.domain.repository.empleado.EmpleadoRepository
import edu.ucne.registroocupaciones.domain.usecase.empleado.GetEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.usecase.ocupacion.GetOcupacionUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import junit.framework.TestCase.assertEquals

@ExperimentalCoroutinesApi
class GetEmpleadoUseCaseTest {
    private lateinit var repository: EmpleadoRepository
    private lateinit var useCase: GetEmpleadoUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetEmpleadoUseCase(repository)
    }

    @Test
    fun `returns task when repository finds it`() = runTest {
        coEvery { repository.getEmpleado(1) } returns Empleado(empleadoId = 1, fechaIngreso = LocalDate.now(), nombres = "Juan", sexo = "Masculino", sueldo = 30000.0)

        val result = useCase(1)

        assertEquals("Juan", result?.nombres)
    }

    @Test
    fun `returns null when repository returns null`() = runTest{
        coEvery { repository.getEmpleado(999) } returns null

        val result = useCase(999)

        assertEquals(null, result)
    }
}