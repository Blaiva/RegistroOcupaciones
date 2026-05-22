package edu.ucne.registroocupaciones.domain.empleado

import edu.ucne.registroocupaciones.domain.model.empleado.Empleado
import edu.ucne.registroocupaciones.domain.repository.empleado.EmpleadoRepository
import edu.ucne.registroocupaciones.domain.usecase.empleado.UpsertEmpleadoUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.assertEquals
@ExperimentalCoroutinesApi
class UpsertEmpleadoUseCaseTest {
    private lateinit var useCase: UpsertEmpleadoUseCase
    private lateinit var repository: EmpleadoRepository

    @Before
    fun setup(){
        repository = mockk()
        useCase = UpsertEmpleadoUseCase(repository)
    }

    @Test
    fun `invoke guarda empleado con datos validos`() = runTest {
        val empleado = Empleado(empleadoId = 0, fechaIngreso = LocalDate.now(), nombres = "Juan", "Masculino", 30000.0)
        coEvery { repository.upsert(empleado) } returns 1

        val result = useCase(empleado)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull())
        coVerify { repository.upsert(empleado) }
    }

    @Test
    fun `invoke falla con nombre vacio`() = runTest {
        val empleado = Empleado(empleadoId = 0, fechaIngreso = LocalDate.now(), nombres = "", "Masculino", 30000.0)

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke falla con nombre muy corto`() = runTest {
        val empleado = Empleado(empleadoId = 0, fechaIngreso = LocalDate.now(), nombres = "A", "Masculino", 30000.0)

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke falla con nombre con caracteres invalidos`() = runTest {
        val empleado = Empleado(empleadoId = 0, fechaIngreso = LocalDate.now(), nombres = "A1fred0", "Masculino", 30000.0)

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke falla con sueldo invalido`() = runTest{
        val empleado = Empleado(empleadoId = 0, fechaIngreso = LocalDate.now(), nombres = "Alfredo", "Masculino", -30000.0)

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke falla con sexo vacio`() = runTest {
        val empleado = Empleado(empleadoId = 0, fechaIngreso = LocalDate.now(), nombres = "Alfredo", "", 30000.0)

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke falla con fecha posterior a la actual`() = runTest {
        val empleado = Empleado(empleadoId = 0, fechaIngreso = LocalDate.now().plusYears(1), nombres = "Alfredo", "Masculino", 30000.0)

        val result = useCase(empleado)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }
}