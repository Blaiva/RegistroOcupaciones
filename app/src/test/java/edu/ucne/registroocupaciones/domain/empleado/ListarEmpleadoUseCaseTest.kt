package edu.ucne.registroocupaciones.domain.empleado

import edu.ucne.registroocupaciones.domain.model.empleado.Empleado
import edu.ucne.registroocupaciones.domain.repository.empleado.EmpleadoRepository
import edu.ucne.registroocupaciones.domain.usecase.empleado.EliminarEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.usecase.empleado.ListarEmpleadoUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import junit.framework.TestCase.assertEquals

@ExperimentalCoroutinesApi
class ListarEmpleadoUseCaseTest {
    private lateinit var repository: EmpleadoRepository
    private lateinit var useCase: ListarEmpleadoUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = ListarEmpleadoUseCase(repository)
    }

    @Test
    fun `invoke llama al repositorio y retorna un flujo`() = runTest {
        val listaEsperada = listOf(
            Empleado(empleadoId = 1, fechaIngreso = LocalDate.now().minusYears(1), nombres = "Juan", "Masculino", 30000.0),
            Empleado(empleadoId = 2, fechaIngreso = LocalDate.now().minusYears(2), nombres = "Juana", "Femenino", 20000.0)
        )
        coEvery { repository.observeEmpleados() } returns flowOf(listaEsperada)

        val result = useCase().first()

        assertEquals(listaEsperada, result)
        coVerify (exactly = 1) {repository.observeEmpleados()}
    }

    @Test
    fun `invoke retorna un flujo vacio cuando no hay registros`() = runTest {
        val listaVacia = emptyList<Empleado>()
        coEvery { repository.observeEmpleados() } returns flowOf(listaVacia)

        val result = useCase().first()

        assertEquals(listaVacia, result)
        coVerify (exactly = 1) {repository.observeEmpleados()}
    }
}