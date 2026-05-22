package edu.ucne.registroocupaciones.domain.ocupacion

import edu.ucne.registroocupaciones.domain.model.ocupacion.Ocupacion
import edu.ucne.registroocupaciones.domain.repository.ocupacion.OcupacionRepository
import edu.ucne.registroocupaciones.domain.usecase.ocupacion.ListarOcupacionesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import junit.framework.TestCase.assertEquals

@ExperimentalCoroutinesApi
class ListarOcupacionUseCaseTest {
    private lateinit var repository: OcupacionRepository
    private lateinit var useCase: ListarOcupacionesUseCase

    @Before
    fun setup(){
        repository = mockk()
        useCase = ListarOcupacionesUseCase(repository)
    }

    @Test
    fun `invoke llama al repositorio y retorna un flujo`() = runTest {
        val listaEsperada = listOf(
            Ocupacion(ocupacionId = 1, descripcion = "Ingeniero", sueldo = 5000.0),
            Ocupacion(ocupacionId = 2, descripcion = "Profesor", sueldo = 8000.0)
        )
        coEvery { repository.observeOcupaciones() } returns flowOf(listaEsperada)

        val result = useCase().first()

        assertEquals(listaEsperada, result)
        coVerify (exactly = 1) {repository.observeOcupaciones()}
    }

    @Test
    fun `invoke retorna un flujo vacio cuando no hay registros`() = runTest {
        val listaVacia = emptyList<Ocupacion>()
        coEvery { repository.observeOcupaciones() } returns flowOf(listaVacia)

        val result = useCase().first()

        assertEquals(listaVacia, result)
        coVerify(exactly = 1) {repository.observeOcupaciones()}
    }
}