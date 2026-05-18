package edu.ucne.registroocupaciones.domain.ocupacion

import edu.ucne.registroocupaciones.domain.repository.ocupacion.OcupacionRepository
import edu.ucne.registroocupaciones.domain.usecase.ocupacion.EliminarOcupacionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class EliminarOcupacionUseCaseTest {
    private lateinit var repository: OcupacionRepository
    private lateinit var useCase: EliminarOcupacionUseCase

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        useCase = EliminarOcupacionUseCase(repository)
    }

    @Test
    fun `calls repository delete with id`() = runTest {
        coEvery { repository.delete(5) } just runs

        useCase(5)

        coVerify { repository.delete(5) }
    }
}