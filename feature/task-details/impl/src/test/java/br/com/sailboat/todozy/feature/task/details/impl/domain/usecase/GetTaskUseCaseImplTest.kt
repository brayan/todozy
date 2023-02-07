package br.com.sailboat.todozy.feature.task.details.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

internal class GetTaskUseCaseImplTest {

    private val repository: TaskRepository = mockk(relaxed = true)

    private val getTaskUseCase = GetTaskUseCaseImpl(repository)

    @Test
    fun `should get task from repository`() = runBlocking {
        val taskId = 42L
        val taskResult: Result<Task> = Result.success(
            Task(
                id = taskId,
                name = "Task Name",
                notes = "Some notes",
            )
        )
        prepareScenario()

        val result = getTaskUseCase(taskId)

        coVerify { repository.getTask(taskId) }
        confirmVerified(repository)
        assertEquals(taskResult, result)
    }

    private fun prepareScenario(
        taskResult: Result<Task> = Result.success(
            Task(
                id = 42L,
                name = "Task Name",
                notes = "Some notes",
            )
        )
    ) {
        coEvery { repository.getTask(any()) } returns taskResult
    }
}
