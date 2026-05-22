package edu.ucne.registroocupaciones.presentation.ocupacion.form

import androidx.lifecycle.SavedStateHandle
import edu.ucne.registroocupaciones.domain.model.ocupacion.Ocupacion
import edu.ucne.registroocupaciones.domain.repository.ocupacion.OcupacionRepository
import edu.ucne.registroocupaciones.domain.usecase.ocupacion.EliminarOcupacionUseCase
import edu.ucne.registroocupaciones.domain.usecase.ocupacion.GetOcupacionUseCase
import edu.ucne.registroocupaciones.domain.usecase.ocupacion.UpsertOcupacionUseCase
import edu.ucne.registroocupaciones.presentation.form.ocupacion.OcupacionFormUiEvent
import edu.ucne.registroocupaciones.presentation.form.ocupacion.OcupacionFormViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class OcupacionFormViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    private lateinit var repository: OcupacionRepository
    private lateinit var getOcupacionUseCase: GetOcupacionUseCase
    private lateinit var upsertOcupacionUseCase: UpsertOcupacionUseCase
    private lateinit var eliminarOcupacionUseCase: EliminarOcupacionUseCase
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        repository = mockk()
        getOcupacionUseCase = mockk()
        upsertOcupacionUseCase = mockk()
        eliminarOcupacionUseCase = mockk()
        savedStateHandle = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `load with null id sets new state`() = runTest(dispatcher) {
        val vm = OcupacionFormViewModel(repository, getOcupacionUseCase, upsertOcupacionUseCase, eliminarOcupacionUseCase, savedStateHandle)

        vm.onEvent(OcupacionFormUiEvent.Load(0))
        runCurrent()

        assertTrue(vm.state.value.isNew)
        assertEquals(null, vm.state.value.ocupacionId)
    }

    @Test
    fun `load with id populates fields`() = runTest(dispatcher) {
        coEvery { getOcupacionUseCase(7) } returns Ocupacion(
            ocupacionId = 7,
            descripcion = "Desarrollador",
            sueldo = 50000.0
        )

        val vm = OcupacionFormViewModel(repository, getOcupacionUseCase, upsertOcupacionUseCase, eliminarOcupacionUseCase, savedStateHandle)

        vm.onEvent(OcupacionFormUiEvent.Load(7))
        runCurrent()

        assertFalse(vm.state.value.isNew)
        assertEquals(7, vm.state.value.ocupacionId)
        assertEquals("Desarrollador", vm.state.value.descripcion)
        assertEquals(50000.0, (vm.state.value.sueldo).toDouble())
    }

    @Test
    fun `save with invalid inputs sets errors and does not save`() = runTest(dispatcher) {
        every { repository.observeOcupaciones() } returns flowOf(emptyList())

        val vm = OcupacionFormViewModel(repository, getOcupacionUseCase, upsertOcupacionUseCase, eliminarOcupacionUseCase, savedStateHandle)

        vm.onEvent(OcupacionFormUiEvent.DescripcionChanged(""))
        vm.onEvent(OcupacionFormUiEvent.SueldoChanged(""))

        vm.onEvent(OcupacionFormUiEvent.Save)
        runCurrent()

        assertNotNull(vm.state.value.descripcionError)
        assertNotNull(vm.state.value.sueldoError)
        assertFalse(vm.state.value.saved)
    }

    @Test
    fun `save with valid inputs calls upsert and sets saved true`() = runTest(dispatcher){
        every { repository.observeOcupaciones() } returns flowOf(emptyList())
        coEvery { upsertOcupacionUseCase(any()) } returns Result.success(123)

        val vm = OcupacionFormViewModel(repository, getOcupacionUseCase, upsertOcupacionUseCase, eliminarOcupacionUseCase, savedStateHandle)

        vm.onEvent(OcupacionFormUiEvent.DescripcionChanged("Arquitecto"))
        vm.onEvent(OcupacionFormUiEvent.SueldoChanged("65000.0"))

        vm.onEvent(OcupacionFormUiEvent.Save)
        runCurrent()

        assertFalse(vm.state.value.isSaving)
        assertTrue(vm.state.value.saved)
        assertEquals(123, vm.state.value.ocupacionId)
    }

    @Test
    fun `delete when has id calls use case and flags deleted`() = runTest(dispatcher) {
        coEvery { getOcupacionUseCase(9) } returns Ocupacion(9, "Medico", 80000.0)
        coEvery { eliminarOcupacionUseCase(9) } returns Unit

        val vm = OcupacionFormViewModel(repository, getOcupacionUseCase, upsertOcupacionUseCase, eliminarOcupacionUseCase, savedStateHandle)

        vm.onEvent(OcupacionFormUiEvent.Load(9))
        runCurrent()

        vm.onEvent(OcupacionFormUiEvent.Delete)
        runCurrent()

        coVerify { eliminarOcupacionUseCase(9) }
        assertFalse(vm.state.value.isDeleting)
        assertTrue(vm.state.value.deleted)
    }
}