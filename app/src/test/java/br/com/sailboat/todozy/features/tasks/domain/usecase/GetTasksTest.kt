package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.model.TaskCategory
import br.com.sailboat.todozy.features.tasks.domain.model.TaskFilter
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository
import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTasks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetTasksTest {

    private val repository: TaskRepository = mockk(relaxed = true)

    private lateinit var getTasks: GetTasks

    private val tasks = listOf(
            Task(id = 45, name = "Task 1", notes = "Some notes"),
            Task(id = 58, name = "Task 2", notes = "Some notes")
    )

    @Before
    fun setUp() {
        getTasks = GetTasks(repository)
    }

    @Test
    fun `should get tasks from repository with alarms triggered before now`() = runBlocking {
        coEvery { repository.getBeforeNowTasks() } returns tasks

        val result = getTasks(TaskFilter(TaskCategory.BEFORE_NOW))

        coVerify(exactly = 1) { repository.getBeforeNowTasks() }
        coVerify(exactly = 0) { repository.getBeforeTodayTasks(any()) }
        coVerify(exactly = 0) { repository.getTodayTasks(any()) }
        coVerify(exactly = 0) { repository.getTomorrowTasks(any()) }
        coVerify(exactly = 0) { repository.getNextDaysTasks(any()) }
        confirmVerified(repository)
        assertEquals(tasks, result)
    }

    @Test
    fun `should get tasks from repository with alarms triggered before today`() = runBlocking {
        coEvery { repository.getBeforeTodayTasks(any()) } returns tasks

        val result = getTasks(TaskFilter(TaskCategory.BEFORE_TODAY))

        coVerify(exactly = 0) { repository.getBeforeNowTasks() }
        coVerify(exactly = 1) { repository.getBeforeTodayTasks(any()) }
        coVerify(exactly = 0) { repository.getTodayTasks(any()) }
        coVerify(exactly = 0) { repository.getTomorrowTasks(any()) }
        coVerify(exactly = 0) { repository.getNextDaysTasks(any()) }
        confirmVerified(repository)
        assertEquals(tasks, result)
    }

    @Test
    fun `should get tasks from repository with alarms for today or without any alarm`() = runBlocking {
        coEvery { repository.getTodayTasks(any()) } returns tasks

        val result = getTasks(TaskFilter(TaskCategory.TODAY))

        coVerify(exactly = 0) { repository.getBeforeNowTasks() }
        coVerify(exactly = 0) { repository.getBeforeTodayTasks(any()) }
        coVerify(exactly = 1) { repository.getTodayTasks(any()) }
        coVerify(exactly = 0) { repository.getTomorrowTasks(any()) }
        coVerify(exactly = 0) { repository.getNextDaysTasks(any()) }
        confirmVerified(repository)
        assertEquals(tasks, result)
    }

    @Test
    fun `should get tasks from repository with alarms for tomorrow`() = runBlocking {
        coEvery { repository.getTomorrowTasks(any()) } returns tasks

        val result = getTasks(TaskFilter(TaskCategory.TOMORROW))

        coVerify(exactly = 0) { repository.getBeforeNowTasks() }
        coVerify(exactly = 0) { repository.getBeforeTodayTasks(any()) }
        coVerify(exactly = 0) { repository.getTodayTasks(any()) }
        coVerify(exactly = 1) { repository.getTomorrowTasks(any()) }
        coVerify(exactly = 0) { repository.getNextDaysTasks(any()) }
        confirmVerified(repository)
        assertEquals(tasks, result)
    }

    @Test
    fun `should get tasks from repository with alarms for next days`() = runBlocking {
        coEvery { repository.getNextDaysTasks(any()) } returns tasks

        val result = getTasks(TaskFilter(TaskCategory.NEXT_DAYS))

        coVerify(exactly = 0) { repository.getBeforeNowTasks() }
        coVerify(exactly = 0) { repository.getBeforeTodayTasks(any()) }
        coVerify(exactly = 0) { repository.getTodayTasks(any()) }
        coVerify(exactly = 0) { repository.getTomorrowTasks(any()) }
        coVerify(exactly = 1) { repository.getNextDaysTasks(any()) }
        confirmVerified(repository)
        assertEquals(tasks, result)
    }

}