package edu.ucne.registroocupaciones.domain.ocupacion

import edu.ucne.registroocupaciones.domain.model.ocupacion.Ocupacion
import edu.ucne.registroocupaciones.domain.repository.ocupacion.OcupacionRepository
import edu.ucne.registroocupaciones.domain.usecase.ocupacion.GetOcupacionUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import junit.framework.TestCase.assertEquals

@ExperimentalCoroutinesApi
class GetOcupacionUseCaseTest {
    private lateinit var repository: OcupacionRepository
    private lateinit var useCase: GetOcupacionUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetOcupacionUseCase(repository)
    }

    @Test
    fun `returns task when repository finds it`() = runTest {
        coEvery { repository.getOcupacion(1) } returns Ocupacion(1, "Ingeniero", sueldo = 10000.0)

        val result = useCase(1)

        assertEquals("Ingeniero", result?.descripcion)
    }

    @Test
    fun `returns null when repository returns null`() = runTest {
        coEvery { repository.getOcupacion(999) } returns null

        val result  = useCase(999)

        assertEquals(null, result)
    }
}