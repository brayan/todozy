package br.com.sailboat.todozy.feature.task.history.impl.domain.usecase

import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistory
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class GetTaskHistoryTest {

    private val repository: TaskHistoryRepository = mockk(relaxed = true)

    private val getTaskHistory = GetTaskHistory(repository)

    @Test
    fun `should get task history from previous days from repository`() = runBlocking {
        val filter = TaskHistoryFilter(category = TaskHistoryCategory.PREVIOUS_DAYS)
        val taskHistoryListResult = Result.success(
            listOf(
                TaskHistory(
                    id = 2,
                    taskId = 22,
                    taskName = "Task 1",
                    status = TaskStatus.DONE,
                    insertingDate = "2020-08-29-13-26-56",
                )
            )
        )
        prepareScenario(taskHistoryListResult = taskHistoryListResult)

        val result: Result<List<TaskHistory>> = getTaskHistory(filter)

        coVerify(exactly = 1) { repository.getPreviousDaysHistory(any()) }
        coVerify(exactly = 0) { repository.getYesterdayHistory(any()) }
        coVerify(exactly = 0) { repository.getTodayHistory(any()) }

        confirmVerified(repository)
        assertEquals(taskHistoryListResult, result)
    }

    @Test
    fun `should get task history from yesterday from repository`() = runBlocking {
        val filter = TaskHistoryFilter(category = TaskHistoryCategory.YESTERDAY)
        val taskHistoryList = listOf(
            TaskHistory(
                id = 2,
                taskId = 22,
                taskName = "Task 1",
                status = TaskStatus.DONE,
                insertingDate = "2020-08-29-13-26-56",
            )
        )
        prepareScenario(taskHistoryListResult = Result.success(taskHistoryList))

        val result = getTaskHistory(filter).getOrNull()

        coVerify(exactly = 0) { repository.getPreviousDaysHistory(any()) }
        coVerify(exactly = 1) { repository.getYesterdayHistory(any()) }
        coVerify(exactly = 0) { repository.getTodayHistory(any()) }

        confirmVerified(repository)
        assertEquals(taskHistoryList, result)
    }

    @Test
    fun `should get task history from today from repository`() = runBlocking {
        val filter = TaskHistoryFilter(category = TaskHistoryCategory.TODAY)
        val taskHistoryList = listOf(
            TaskHistory(
                id = 2,
                taskId = 22,
                taskName = "Task 1",
                status = TaskStatus.DONE,
                insertingDate = "2020-08-29-13-26-56",
            )
        )
        prepareScenario(taskHistoryListResult = Result.success(taskHistoryList))

        val result = getTaskHistory(filter).getOrNull()

        coVerify(exactly = 0) { repository.getPreviousDaysHistory(any()) }
        coVerify(exactly = 0) { repository.getYesterdayHistory(any()) }
        coVerify(exactly = 1) { repository.getTodayHistory(any()) }

        confirmVerified(repository)
        assertEquals(taskHistoryList, result)
    }

    private fun prepareScenario(
        taskHistoryListResult: Result<List<TaskHistory>> = Result.success(
            listOf(
                TaskHistory(
                    id = 2,
                    taskId = 22,
                    taskName = "Task 1",
                    status = TaskStatus.DONE,
                    insertingDate = "2020-08-29-13-26-56"
                ),
            )
        )
    ) {
        coEvery { repository.getTodayHistory(any()) } returns taskHistoryListResult
        coEvery { repository.getPreviousDaysHistory(any()) } returns taskHistoryListResult
        coEvery { repository.getYesterdayHistory(any()) } returns taskHistoryListResult
    }

}