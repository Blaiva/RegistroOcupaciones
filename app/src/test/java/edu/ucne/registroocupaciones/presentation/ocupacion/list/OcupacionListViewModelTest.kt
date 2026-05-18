package edu.ucne.registroocupaciones.presentation.ocupacion.list

import edu.ucne.registroocupaciones.domain.model.ocupacion.Ocupacion
import edu.ucne.registroocupaciones.domain.usecase.ocupacion.EliminarOcupacionUseCase
import edu.ucne.registroocupaciones.domain.usecase.ocupacion.ListarOcupacionesUseCase
import edu.ucne.registroocupaciones.presentation.list.ocupacion.OcupacionListUiEvent
import edu.ucne.registroocupaciones.presentation.list.ocupacion.OcupacionListViewModel
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
class OcupacionListViewModelTest {
    private  val dispatcher = StandardTestDispatcher()
    private lateinit var listarOcupacionesUseCase: ListarOcupacionesUseCase
    private lateinit var eliminarOcupacionUseCase: EliminarOcupacionUseCase

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        listarOcupacionesUseCase = mockk()
        eliminarOcupacionUseCase = mockk()
    }

    @Test
    fun `delete calls usecase and shows message`() = runTest(dispatcher) {
        val shared = MutableSharedFlow<List<Ocupacion>>(replay = 1)
        shared.emit(emptyList())
        every { listarOcupacionesUseCase() } returns shared
        coEvery { eliminarOcupacionUseCase(5) } returns Unit

        val vm = OcupacionListViewModel(listarOcupacionesUseCase, eliminarOcupacionUseCase)
        runCurrent()

        vm.onEvent(OcupacionListUiEvent.Delete(5))
        runCurrent()

        coVerify { eliminarOcupacionUseCase(5) }
        assertEquals("Eliminado", vm.state.value.message)
    }

    @Test
    fun `navigation flags toggle as expected`() = runTest(dispatcher){
        val shared = MutableSharedFlow<List<Ocupacion>>(replay = 1)
        shared.emit(emptyList())
        every { listarOcupacionesUseCase() } returns shared

        val vm = OcupacionListViewModel(listarOcupacionesUseCase, eliminarOcupacionUseCase)
        runCurrent()

        vm.onEvent(OcupacionListUiEvent.CreateNew)
        assertTrue(vm.state.value.navigateToCreate)

        vm.onEvent(OcupacionListUiEvent.Edit(10))
        assertEquals(10, vm.state.value.navigateToEditId)
    }
}