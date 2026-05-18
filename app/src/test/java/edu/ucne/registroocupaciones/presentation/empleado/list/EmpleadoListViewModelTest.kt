package edu.ucne.registroocupaciones.presentation.empleado.list

import edu.ucne.registroocupaciones.domain.model.empleado.Empleado
import edu.ucne.registroocupaciones.domain.usecase.empleado.EliminarEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.usecase.empleado.ListarEmpleadoUseCase
import edu.ucne.registroocupaciones.presentation.list.empleado.EmpleadoListUiEvent
import edu.ucne.registroocupaciones.presentation.list.empleado.EmpleadoListViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue

@ExperimentalCoroutinesApi
class EmpleadoListViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var listarEmpleadoUseCase: ListarEmpleadoUseCase
    private lateinit var eliminarEmpleadoUseCase: EliminarEmpleadoUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        listarEmpleadoUseCase = mockk()
        eliminarEmpleadoUseCase = mockk()
    }

    @Test
    fun `delete calls usecase and shows message`() = runTest(dispatcher) {
        val shared = MutableSharedFlow<List<Empleado>>(replay = 1)
        shared.emit(emptyList())
        every { listarEmpleadoUseCase() } returns shared
        coEvery { eliminarEmpleadoUseCase(5) } returns Unit

        val vm = EmpleadoListViewModel(listarEmpleadoUseCase, eliminarEmpleadoUseCase)
        runCurrent()

        vm.onEvent(EmpleadoListUiEvent.Delete(5))
        runCurrent()

        coVerify { eliminarEmpleadoUseCase(5) }
        assertEquals("Eliminado", vm.state.value.message)
    }

    @Test
    fun `navigation flags toggle as expected`() = runTest(dispatcher) {
        val shared = MutableSharedFlow<List<Empleado>>(replay = 1)
        shared.emit(emptyList())
        every { listarEmpleadoUseCase() } returns shared

        val vm = EmpleadoListViewModel(listarEmpleadoUseCase, eliminarEmpleadoUseCase)
        runCurrent()

        vm.onEvent(EmpleadoListUiEvent.CreateNew)
        assertTrue(vm.state.value.navigateToCreate)

        vm.onEvent(EmpleadoListUiEvent.Edit(10))
        assertEquals(10, vm.state.value.navigateToEditId)
    }
}