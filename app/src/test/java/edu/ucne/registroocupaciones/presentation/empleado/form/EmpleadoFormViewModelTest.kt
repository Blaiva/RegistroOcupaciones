package edu.ucne.registroocupaciones.presentation.empleado.form

import androidx.lifecycle.SavedStateHandle
import edu.ucne.registroocupaciones.domain.model.empleado.Empleado
import edu.ucne.registroocupaciones.domain.repository.empleado.EmpleadoRepository
import edu.ucne.registroocupaciones.domain.usecase.empleado.EliminarEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.usecase.empleado.GetEmpleadoUseCase
import edu.ucne.registroocupaciones.domain.usecase.empleado.UpsertEmpleadoUseCase
import edu.ucne.registroocupaciones.presentation.form.empleado.EmpleadoFormUiEvent
import edu.ucne.registroocupaciones.presentation.form.empleado.EmpleadoFormViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

@ExperimentalCoroutinesApi
class EmpleadoFormViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    private lateinit var repository: EmpleadoRepository
    private lateinit var getEmpleadoUseCase: GetEmpleadoUseCase
    private lateinit var upsertEmpleadoUseCase: UpsertEmpleadoUseCase
    private lateinit var eliminarEmpleadoUseCase: EliminarEmpleadoUseCase
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp(){
        Dispatchers.setMain(dispatcher)
        repository = mockk()
        getEmpleadoUseCase = mockk()
        upsertEmpleadoUseCase = mockk()
        eliminarEmpleadoUseCase = mockk()
        savedStateHandle = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `load with null id sets new state`() = runTest(dispatcher){
        val vm = EmpleadoFormViewModel(repository, getEmpleadoUseCase, upsertEmpleadoUseCase, eliminarEmpleadoUseCase, savedStateHandle)

        vm.onEvent(EmpleadoFormUiEvent.Load(0))
        runCurrent()

        assertTrue(vm.state.value.isNew)
        assertNull(vm.state.value.empleadoId)
    }

    @Test
    fun `load with id populates fields`() = runTest(dispatcher) {
        coEvery { getEmpleadoUseCase(7) } returns Empleado(
            empleadoId = 7,
            fechaIngreso = LocalDate.now(),
            nombres = "Juan",
            sexo = "Masculino",
            sueldo = 50000.0
        )

        val vm = EmpleadoFormViewModel(repository, getEmpleadoUseCase, upsertEmpleadoUseCase, eliminarEmpleadoUseCase, savedStateHandle)

        vm.onEvent(EmpleadoFormUiEvent.Load(7))
        runCurrent()

        assertFalse(vm.state.value.isNew)
        assertEquals(7, vm.state.value.empleadoId)
        assertEquals(LocalDate.now(), vm.state.value.fechaIngreso)
        assertEquals("Juan", vm.state.value.nombres)
        assertEquals("Masculino", vm.state.value.sexo)
        assertEquals(50000.0, (vm.state.value.sueldo).toDouble())
    }

    @Test
    fun `save with invalid inputs sets errors and does not save`() = runTest(dispatcher){
        every { repository.observeEmpleados() } returns flowOf(emptyList())

        val vm = EmpleadoFormViewModel(repository, getEmpleadoUseCase, upsertEmpleadoUseCase, eliminarEmpleadoUseCase, savedStateHandle)

        vm.onEvent(EmpleadoFormUiEvent.FechaIngresoChanged(LocalDate.now().plusYears(1)))
        vm.onEvent(EmpleadoFormUiEvent.NombresChanged(""))
        vm.onEvent(EmpleadoFormUiEvent.SexoChanged(""))
        vm.onEvent(EmpleadoFormUiEvent.SueldoChanged(""))

        vm.onEvent(EmpleadoFormUiEvent.Save)
        runCurrent()

        assertNotNull(vm.state.value.fechaIngresoError)
        assertNotNull(vm.state.value.nombresError)
        assertNotNull(vm.state.value.sexoError)
        assertNotNull(vm.state.value.sueldoError)
        assertFalse(vm.state.value.saved)
    }

    @Test
    fun `save with valid inputs calls upsert and sets saved true`() = runTest(dispatcher) {
        every { repository.observeEmpleados() } returns flowOf(emptyList())
        coEvery { upsertEmpleadoUseCase(any()) } returns Result.success(123)

        val vm = EmpleadoFormViewModel(repository, getEmpleadoUseCase, upsertEmpleadoUseCase, eliminarEmpleadoUseCase, savedStateHandle)

        vm.onEvent(EmpleadoFormUiEvent.FechaIngresoChanged(LocalDate.now()))
        vm.onEvent(EmpleadoFormUiEvent.NombresChanged("Juan"))
        vm.onEvent(EmpleadoFormUiEvent.SexoChanged("Masculino"))
        vm.onEvent(EmpleadoFormUiEvent.SueldoChanged("65000.0"))

        vm.onEvent(EmpleadoFormUiEvent.Save)
        runCurrent()

        assertFalse(vm.state.value.isSaving)
        assertTrue(vm.state.value.saved)
        assertEquals(123, vm.state.value.empleadoId)
    }

    @Test
    fun `delete when has id calls use case and flags deleted`() = runTest(dispatcher) {
        coEvery { getEmpleadoUseCase(9) } returns Empleado(
            empleadoId = 9,
            fechaIngreso = LocalDate.now(),
            nombres = "Juan",
            sexo = "Masculino",
            sueldo = 80000.0
        )
        coEvery { eliminarEmpleadoUseCase(9) } returns Unit

        val vm = EmpleadoFormViewModel(repository, getEmpleadoUseCase, upsertEmpleadoUseCase, eliminarEmpleadoUseCase, savedStateHandle)

        vm.onEvent(EmpleadoFormUiEvent.Load(9))
        runCurrent()

        vm.onEvent(EmpleadoFormUiEvent.Delete)
        runCurrent()

        coVerify { eliminarEmpleadoUseCase(9) }
        assertFalse(vm.state.value.isDeleting)
        assertTrue(vm.state.value.deleted)
    }
}