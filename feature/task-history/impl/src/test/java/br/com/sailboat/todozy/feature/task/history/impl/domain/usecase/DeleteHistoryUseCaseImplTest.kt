package br.com.sailboat.todozy.feature.task.history.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistory
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class DeleteHistoryUseCaseImplTest {
    private val repository: TaskHistoryRepository = mockk(relaxed = true)

    private val deleteHistoryUseCase = DeleteHistoryUseCaseImpl(repository)

    @Test
    fun `should delete history from repository`() =
        runBlocking {
            val history =
                TaskHistory(
                    taskId = 12,
                    status = TaskStatus.DONE,
                    insertingDate = "2020-08-29-16-18-00",
                    taskName = "Task 1",
                )
            deleteHistoryUseCase(history)

            coVerify { repository.delete(history) }
            confirmVerified(repository)
        }
}
