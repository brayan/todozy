package br.com.sailboat.todozy.feature.task.details.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.repository.TaskRepository
import br.com.sailboat.todozy.feature.alarm.domain.usecase.DeleteAlarmUseCase
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class DisableTaskUseCaseImplTest {
    private val repository: TaskRepository = mockk(relaxed = true)
    private val deleteAlarmUseCase: DeleteAlarmUseCase = mockk(relaxed = true)

    private val disableTaskUseCase = DisableTaskUseCaseImpl(repository, deleteAlarmUseCase)

    private val task = Task(id = Entity.NO_ID, name = "Task Name", notes = "Some notes")

    @Test
    fun `should disable task from repository`() =
        runBlocking {
            disableTaskUseCase(task)

            coVerify { repository.disableTask(task) }
            confirmVerified(repository)
        }

    @Test
    fun `should call deleteAlarm when disable task`() =
        runBlocking {
            disableTaskUseCase(task)

            coVerify { deleteAlarmUseCase(task.id) }
            confirmVerified(deleteAlarmUseCase)
        }
}
