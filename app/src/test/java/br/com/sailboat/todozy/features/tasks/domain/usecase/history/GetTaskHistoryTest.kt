package br.com.sailboat.todozy.features.tasks.domain.usecase.history

import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskHistoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetTaskHistoryTest {

    private val repository: TaskHistoryRepository = mockk(relaxed = true)

    private lateinit var getTaskHistory: GetTaskHistory

    private val history = listOf(
            TaskHistory(
                    id = 2,
                    taskId = 22,
                    taskName = "Task 1",
                    status = TaskStatus.DONE,
                    insertingDate = "2020-08-29-13-26-56"),
            TaskHistory(id = 3,
                    taskId = 23,
                    taskName = "Task 2",
                    status = TaskStatus.NOT_DONE,
                    insertingDate = "2020-08-29-13-27-39")
    )

    @Before
    fun setUp() {
        getTaskHistory = GetTaskHistory(repository)
    }

    @Test
    fun `should get task history from previous days from repository`() = runBlocking {
        coEvery { repository.getPreviousDaysHistory(any()) } returns history

        val result = getTaskHistory(TaskHistoryFilter(category = TaskHistoryCategory.PREVIOUS_DAYS))

        coVerify(exactly = 1) { repository.getPreviousDaysHistory(any()) }
        coVerify(exactly = 0) { repository.getYesterdayHistory(any()) }
        coVerify(exactly = 0) { repository.getTodayHistory(any()) }

        confirmVerified(repository)
        assertEquals(result, history)
    }

    @Test
    fun `should get task history from yesterday from repository`() = runBlocking {
        coEvery { repository.getYesterdayHistory(any()) } returns history

        val result = getTaskHistory(TaskHistoryFilter(category = TaskHistoryCategory.YESTERDAY))

        coVerify(exactly = 0) { repository.getPreviousDaysHistory(any()) }
        coVerify(exactly = 1) { repository.getYesterdayHistory(any()) }
        coVerify(exactly = 0) { repository.getTodayHistory(any()) }

        confirmVerified(repository)
        assertEquals(result, history)
    }

    @Test
    fun `should get task history from today from repository`() = runBlocking {
        coEvery { repository.getTodayHistory(any()) } returns history

        val result = getTaskHistory(TaskHistoryFilter(category = TaskHistoryCategory.TODAY))

        coVerify(exactly = 0) { repository.getPreviousDaysHistory(any()) }
        coVerify(exactly = 0) { repository.getYesterdayHistory(any()) }
        coVerify(exactly = 1) { repository.getTodayHistory(any()) }

        confirmVerified(repository)
        assertEquals(result, history)
    }

}