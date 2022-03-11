package br.com.sailboat.todozy.feature.task.details.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class GetTaskMetricsTest {

    private val repository: TaskHistoryRepository = mockk(relaxed = true)
    private val getTaskMetrics = GetTaskMetrics(repository)


    @Test
    fun `should get task metrics from repository`() = runBlocking {
        prepareScenario()

        val result = getTaskMetrics(TaskHistoryFilter(taskId = 22)).getOrNull()

        coVerify { repository.getTaskHistory(any()) }
        coVerify { repository.getTotalOfDoneTasks(any()) }
        coVerify { repository.getTotalOfNotDoneTasks(any()) }

        confirmVerified(repository)
        assertEquals(TaskMetrics(doneTasks = 10, notDoneTasks = 5, consecutiveDone = 2), result)
    }

    @Test
    fun `should return consecutiveDone 0 from repository when taskId has NO_ID`() = runBlocking {
        prepareScenario()

        val result = getTaskMetrics(TaskHistoryFilter(taskId = Entity.NO_ID)).getOrNull()

        coVerify(exactly = 0) { repository.getTaskHistory(any()) }
        coVerify { repository.getTotalOfDoneTasks(any()) }
        coVerify { repository.getTotalOfNotDoneTasks(any()) }

        confirmVerified(repository)
        assertEquals(TaskMetrics(doneTasks = 10, notDoneTasks = 5, consecutiveDone = 0), result)
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
                TaskHistory(
                    id = 2,
                    taskId = 22,
                    taskName = "Task 1",
                    status = TaskStatus.DONE,
                    insertingDate = "2020-08-29-13-26-58"
                ),
                TaskHistory(
                    id = 3,
                    taskId = 22,
                    taskName = "Task 1",
                    status = TaskStatus.NOT_DONE,
                    insertingDate = "2020-08-29-13-27-39"
                )
            )
        ),
        totalOfDoneTasksResult: Result<Int> = Result.success(10),
        totalOfNotDoneTasksResult: Result<Int> = Result.success(5),
    ) {
        coEvery { repository.getTaskHistory(any()) } returns taskHistoryListResult
        coEvery { repository.getTotalOfDoneTasks(any()) } returns totalOfDoneTasksResult
        coEvery { repository.getTotalOfNotDoneTasks(any()) } returns totalOfNotDoneTasksResult
    }

}