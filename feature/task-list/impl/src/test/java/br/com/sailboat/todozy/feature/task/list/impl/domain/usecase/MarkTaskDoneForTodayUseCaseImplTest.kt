package br.com.sailboat.todozy.feature.task.list.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.history.domain.usecase.AddHistoryUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertTrue

internal class MarkTaskDoneForTodayUseCaseImplTest {
    private val getTaskUseCase: GetTaskUseCase = mockk(relaxed = true)
    private val addHistoryUseCase: AddHistoryUseCase = mockk(relaxed = true)

    private val markTaskDoneForTodayUseCase =
        MarkTaskDoneForTodayUseCaseImpl(
            getTaskUseCase = getTaskUseCase,
            addHistoryUseCase = addHistoryUseCase,
        )

    @Test
    fun `should add history for task id`() = runBlocking {
        val task =
            Task(
                id = 42L,
                name = "Task Name",
                notes = "Some notes",
            )
        coEvery { getTaskUseCase(task.id) } returns Result.success(task)
        coEvery { addHistoryUseCase(task, TaskStatus.DONE) } returns Result.success(Unit)

        val result = markTaskDoneForTodayUseCase(task.id)

        assertTrue(result.isSuccess)
        coVerify { getTaskUseCase(task.id) }
        coVerify { addHistoryUseCase(task, TaskStatus.DONE) }
        confirmVerified(getTaskUseCase, addHistoryUseCase)
    }
}
