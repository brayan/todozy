package br.com.sailboat.todozy.features.tasks.domain.usecase.history

import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskHistoryRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DeleteHistoryTest {

    private val repository: TaskHistoryRepository = mockk(relaxed = true)

    private lateinit var deleteHistory: DeleteHistory

    @Before
    fun setUp() {
        deleteHistory = DeleteHistory(repository)
    }

    @Test
    fun `should delete history from repository`() = runBlocking {
        val history = TaskHistory(
                taskId = 12,
                status = TaskStatus.DONE,
                insertingDate = "2020-08-29-16-18-00",
                taskName = "Task 1")
        deleteHistory(history)

        coVerify { repository.delete(history) }
        confirmVerified(repository)
    }

}