package edu.ucne.registroocupaciones.data.ocupacion

import edu.ucne.registroocupaciones.data.local.ocupacion.OcupacionDao
import edu.ucne.registroocupaciones.data.local.ocupacion.OcupacionEntity
import edu.ucne.registroocupaciones.data.repository.ocupacion.OcupacionRepositoryImpl
import edu.ucne.registroocupaciones.domain.model.ocupacion.Ocupacion
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import junit.framework.TestCase.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class OcupacionRepositoryImplTest {
    private lateinit var dao: OcupacionDao
    private lateinit var repository: OcupacionRepositoryImpl

    @Before
    fun setUp() {
        dao = mockk(relaxed = true)
        repository = OcupacionRepositoryImpl(dao)
    }

    @Test
    fun `upsert guarda ocupacion correctamente`() = runTest {
        val ocupacion = Ocupacion(
            ocupacionId = 0,
            descripcion = "Nueva ocupacion",
            sueldo = 30.0
        )

        val ocupacionSlot = slot<OcupacionEntity>()
        coEvery { dao.upsert(capture(ocupacionSlot)) } just Runs

        val result = repository.upsert(ocupacion)

        assertEquals(0, result)
        coVerify { dao.upsert(any()) }
        assertEquals("Nueva ocupacion", ocupacionSlot.captured.descripcion)
        assertEquals(30.0, ocupacionSlot.captured.sueldo)
    }

    @Test
    fun `upsert actualiza guarda ocupacion correctamente`() = runTest {
        val ocupacion = Ocupacion(ocupacionId = 1, descripcion = "Ocupacion actualizada", sueldo = 45.0)
        coEvery { dao.upsert(any()) } just Runs

        val result = repository.upsert(ocupacion)

        assertEquals(1, result)
        coVerify { dao.upsert(any()) }
    }

    @Test
    fun `delete elimina ocupacion correctamente`() = runTest {
        val ocupacionId = 1
        coEvery { dao.eliminar(ocupacionId) } just Runs

        repository.delete(ocupacionId)

        coVerify { dao.eliminar(ocupacionId) }
    }

    @Test
    fun `observeOcupaciones retorna flow de ocupaciones`() = runTest {
        val entities = listOf(
            OcupacionEntity(1, "Ocupacion 1", 30.0),
            OcupacionEntity(2, "Ocupacion 2", 45.0)
        )
        every { dao.listar() } returns flowOf(entities)

        val result = repository.observeOcupaciones().first()

        assertEquals(2, result.size)
        assertEquals("Ocupacion 1", result[0].descripcion)
        assertEquals("Ocupacion 2", result[1].descripcion)
    }

    @Test
    fun `buscar guarda ocupacion correctamente`() = runTest {
        val entity = OcupacionEntity(1, "Ocupacion Test", 30.0)
        coEvery { dao.buscar(1) } returns entity

        val result = repository.getOcupacion(1)

        assertNotNull(result)
        assertEquals("Ocupacion Test", result?.descripcion)
        assertEquals(30.0, result?.sueldo)
    }
}