package br.com.sailboat.todozy.feature.task.history.impl.domain.usecase

import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class DeleteAllHistoryUseCaseImplTest {
    private val repository: TaskHistoryRepository = mockk(relaxed = true)

    private val deleteAllHistoryUseCase = DeleteAllHistoryUseCaseImpl(repository)

    @Test
    fun `should delete all history from repository`() = runBlocking {
        deleteAllHistoryUseCase()

        coVerify { repository.deleteAll() }
        confirmVerified(repository)
    }
}
