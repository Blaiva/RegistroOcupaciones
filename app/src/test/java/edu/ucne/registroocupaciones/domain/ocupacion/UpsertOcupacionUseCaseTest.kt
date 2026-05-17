package edu.ucne.registroocupaciones.domain.ocupacion

import edu.ucne.registroocupaciones.data.repository.ocupacion.OcupacionRepositoryImpl
import edu.ucne.registroocupaciones.domain.model.ocupacion.Ocupacion
import edu.ucne.registroocupaciones.domain.repository.empleado.EmpleadoRepository
import edu.ucne.registroocupaciones.domain.repository.ocupacion.OcupacionRepository
import edu.ucne.registroocupaciones.domain.usecase.ocupacion.UpsertOcupacionUseCase
import edu.ucne.registroocupaciones.presentation.list.OcupacionItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkClass
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf

@ExperimentalCoroutinesApi
class UpsertOcupacionUseCaseTest {
    private lateinit var useCase: UpsertOcupacionUseCase
    private lateinit var repository: OcupacionRepository

    @Before
    fun setup() {
        repository = mockk()
        useCase = UpsertOcupacionUseCase(repository)
    }

    @Test
    fun`invoke guarda ocupacion con datos validos`() = runTest {
        val ocupacion = Ocupacion(ocupacionId = 0, descripcion = "Ingeniero", sueldo = 30000.0)
        coEvery { repository.observeOcupaciones() } returns flowOf(emptyList())
        coEvery { repository.upsert(ocupacion) } returns 1

        val result = useCase(ocupacion)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrNull())
        coVerify { repository.upsert(ocupacion) }
    }

    @Test
    fun `invoke falla con descripcion vacia`() = runTest {
        val ocupacion = Ocupacion(ocupacionId = 0, descripcion = "", sueldo = 30000.0)
        coEvery { repository.observeOcupaciones() } returns flowOf(emptyList())

        val result = useCase(ocupacion)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke falla con descripcion muy corta`() = runTest {
        val ocupacion = Ocupacion(ocupacionId = 0,  descripcion = "ab", sueldo = 30000.0)
        coEvery { repository.observeOcupaciones() } returns flowOf(emptyList())

        val result = useCase(ocupacion)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke falla cuando la ocupacion ya esta registrada`() = runTest {
        val ocupacionesExistentes = listOf(Ocupacion(ocupacionId = 1, descripcion = "Ingeniero", sueldo = 30000.0))
        coEvery { repository.observeOcupaciones() } returns flowOf(ocupacionesExistentes)

        val ocupacion = Ocupacion(ocupacionId = 0, descripcion = "Ingeniero", sueldo = 35000.0)

        val result = useCase(ocupacion)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }

    @Test
    fun `invoke falla con sueldo invalido`() = runTest {
        val ocupacion = Ocupacion(ocupacionId = 0, descripcion = "Test Ocupacion", sueldo = -5000.0)
        coEvery { repository.observeOcupaciones() } returns flowOf(emptyList())

        val result = useCase(ocupacion)

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }
}