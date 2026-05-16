package edu.ucne.registroocupaciones.data.empleado

import edu.ucne.registroocupaciones.data.local.empleado.EmpleadoDao
import edu.ucne.registroocupaciones.data.local.empleado.EmpleadoEntity
import edu.ucne.registroocupaciones.data.repository.empleado.EmpleadoRepositoryImpl
import edu.ucne.registroocupaciones.domain.model.empleado.Empleado
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import junit.framework.TestCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class EmpleadoRepositoryImplTest {
    private lateinit var dao: EmpleadoDao
    private lateinit var repository: EmpleadoRepositoryImpl

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        repository = EmpleadoRepositoryImpl(dao)
    }

    @Test
    fun `upsert guarda ocupacion correctamente`()  = runTest {
        val empleado = Empleado(
            empleadoId = 0,
            fechaIngreso = LocalDate.now(),
            nombres = "Juan",
            sexo = "Masculino",
            sueldo = 10000.0
        )

        val empleadoSlot = slot<EmpleadoEntity>()
        coEvery { dao.upsert(capture(empleadoSlot)) } just Runs

        val result = repository.upsert(empleado)

        TestCase.assertEquals(0, result)
        coVerify { dao.upsert(any()) }
        TestCase.assertEquals(LocalDate.now(), empleadoSlot.captured.fechaIngreso)
        TestCase.assertEquals("Juan", empleadoSlot.captured.nombres)
        TestCase.assertEquals("Masculino", empleadoSlot.captured.sexo)
        TestCase.assertEquals(10000.0, empleadoSlot.captured.sueldo)
    }

    @Test
    fun `upsert actualiza guarda ocupacion correctamente`() = runTest {
        val empleado = Empleado(
            empleadoId = 1,
            fechaIngreso = LocalDate.now().minusYears(1),
            nombres = "Juana",
            sexo = "Femenino",
            sueldo = 8000.0
        )
        coEvery { dao.upsert(any()) } just Runs

        val result = repository.upsert(empleado)

        TestCase.assertEquals(1, result)
        coVerify { dao.upsert(any()) }
    }

    @Test
    fun `delete elimina ocupacion correctamente`() = runTest {
        val empleadoId = 1
        coEvery { dao.eliminar((empleadoId)) } just Runs

        repository.delete(empleadoId)

        coVerify { dao.eliminar(empleadoId) }
    }

    @Test
    fun `observeEmpleados retorna flow de ocupaciones`() = runTest {
        val entities = listOf(
            EmpleadoEntity(1, LocalDate.now().minusYears(1), "Juan", "Masculino", 10000.0),
            EmpleadoEntity(2, LocalDate.now().minusYears(2), "Maria", "Femenino", 8000.0),
        )
        every { dao.listar() } returns flowOf(entities)

        val result = repository.observeEmpleados().first()

        TestCase.assertEquals(2, result.size)
        TestCase.assertEquals("Juan", result[0].nombres)
        TestCase.assertEquals("Maria", result[1].nombres)
    }

    @Test
    fun `buscar retorna ocupacion correctamente`() = runTest {
        val entity = EmpleadoEntity(1, LocalDate.now(), "Juan", "Masculino", 10000.0)
        coEvery { dao.buscar(1) } returns entity

        val result = repository.getEmpleado(1)

        TestCase.assertNotNull(result)
        TestCase.assertEquals(LocalDate.now(), result?.fechaIngreso)
        TestCase.assertEquals("Juan", result?.nombres)
        TestCase.assertEquals("Masculino", result?.sexo)
        TestCase.assertEquals(10000.0, result?.sueldo)
    }
}