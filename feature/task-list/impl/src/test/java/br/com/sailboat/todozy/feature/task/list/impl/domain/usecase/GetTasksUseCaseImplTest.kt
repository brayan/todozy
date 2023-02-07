package br.com.sailboat.todozy.feature.task.list.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.domain.model.TaskFilter
import br.com.sailboat.todozy.domain.model.mock.TaskMockFactory
import br.com.sailboat.todozy.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

internal class GetTasksUseCaseImplTest {

    private val repository: TaskRepository = mockk(relaxed = true)
    private val getTasksUseCase = GetTasksUseCaseImpl(repository)

    @Test
    fun `should get tasks from repository with alarms triggered before now`() = runBlocking {
        val tasksResult = Result.success(TaskMockFactory.makeTaskList())
        prepareScenario(tasksResult = tasksResult)

        val result = getTasksUseCase(TaskFilter(category = TaskCategory.BEFORE_NOW))

        coVerify(exactly = 1) { repository.getBeforeNowTasks() }
        coVerify(exactly = 0) { repository.getBeforeTodayTasks(any()) }
        coVerify(exactly = 0) { repository.getTodayTasks(any()) }
        coVerify(exactly = 0) { repository.getTomorrowTasks(any()) }
        coVerify(exactly = 0) { repository.getNextDaysTasks(any()) }
        confirmVerified(repository)
        assertEquals(tasksResult, result)
    }

    @Test
    fun `should get tasks from repository with alarms triggered before today`() = runBlocking {
        val tasksResult = Result.success(TaskMockFactory.makeTaskList())
        prepareScenario(tasksResult = tasksResult)

        val result = getTasksUseCase(TaskFilter(category = TaskCategory.BEFORE_TODAY))

        coVerify(exactly = 0) { repository.getBeforeNowTasks() }
        coVerify(exactly = 1) { repository.getBeforeTodayTasks(any()) }
        coVerify(exactly = 0) { repository.getTodayTasks(any()) }
        coVerify(exactly = 0) { repository.getTomorrowTasks(any()) }
        coVerify(exactly = 0) { repository.getNextDaysTasks(any()) }
        confirmVerified(repository)
        assertEquals(tasksResult, result)
    }

    @Test
    fun `should get tasks from repository with alarms for today or without any alarm`() =
        runBlocking {
            val tasksResult = Result.success(TaskMockFactory.makeTaskList())
            prepareScenario(tasksResult = tasksResult)

            val result = getTasksUseCase(TaskFilter(category = TaskCategory.TODAY))

            coVerify(exactly = 0) { repository.getBeforeNowTasks() }
            coVerify(exactly = 0) { repository.getBeforeTodayTasks(any()) }
            coVerify(exactly = 1) { repository.getTodayTasks(any()) }
            coVerify(exactly = 0) { repository.getTomorrowTasks(any()) }
            coVerify(exactly = 0) { repository.getNextDaysTasks(any()) }
            confirmVerified(repository)
            assertEquals(tasksResult, result)
        }

    @Test
    fun `should get tasks from repository with alarms for tomorrow`() = runBlocking {
        val tasksResult = Result.success(TaskMockFactory.makeTaskList())
        prepareScenario(tasksResult = tasksResult)

        val result = getTasksUseCase(TaskFilter(category = TaskCategory.TOMORROW))

        coVerify(exactly = 0) { repository.getBeforeNowTasks() }
        coVerify(exactly = 0) { repository.getBeforeTodayTasks(any()) }
        coVerify(exactly = 0) { repository.getTodayTasks(any()) }
        coVerify(exactly = 1) { repository.getTomorrowTasks(any()) }
        coVerify(exactly = 0) { repository.getNextDaysTasks(any()) }
        confirmVerified(repository)
        assertEquals(tasksResult, result)
    }

    @Test
    fun `should get tasks from repository with alarms for next days`() = runBlocking {
        val tasksResult = Result.success(TaskMockFactory.makeTaskList())
        prepareScenario(tasksResult = tasksResult)

        val result = getTasksUseCase(TaskFilter(category = TaskCategory.NEXT_DAYS))

        coVerify(exactly = 0) { repository.getBeforeNowTasks() }
        coVerify(exactly = 0) { repository.getBeforeTodayTasks(any()) }
        coVerify(exactly = 0) { repository.getTodayTasks(any()) }
        coVerify(exactly = 0) { repository.getTomorrowTasks(any()) }
        coVerify(exactly = 1) { repository.getNextDaysTasks(any()) }
        confirmVerified(repository)
        assertEquals(tasksResult, result)
    }

    private fun prepareScenario(
        tasksResult: Result<List<Task>> = Result.success(TaskMockFactory.makeTaskList()),
    ) {
        coEvery { repository.getBeforeNowTasks() } returns tasksResult
        coEvery { repository.getBeforeTodayTasks(any()) } returns tasksResult
        coEvery { repository.getTodayTasks(any()) } returns tasksResult
        coEvery { repository.getTomorrowTasks(any()) } returns tasksResult
        coEvery { repository.getNextDaysTasks(any()) } returns tasksResult
    }
}
