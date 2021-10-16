package br.com.sailboat.todozy.features.tasks.domain.usecase.history

import br.com.sailboat.todozy.features.tasks.domain.repository.TaskHistoryRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DeleteAllHistoryTest {

    private val repository: TaskHistoryRepository = mockk(relaxed = true)

    private val deleteAllHistory = DeleteAllHistory(repository)

    @Test
    fun `should delete all history from repository`() = runBlocking {
        deleteAllHistory()

        coVerify { repository.deleteAll() }
        confirmVerified(repository)
    }

}