package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.utility.kotlin.model.Entity
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.DeleteAlarmUseCase
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DisableTaskTest {

    private val repository: TaskRepository = mockk(relaxed = true)
    private val deleteAlarmUseCase: DeleteAlarmUseCase = mockk(relaxed = true)

    private val disableTask = DisableTask(repository, deleteAlarmUseCase)

    private val task = Task(id = Entity.NO_ID, name = "Task Name", notes = "Some notes")

    @Test
    fun `should disable task from repository`() = runBlocking {
        disableTask(task)

        coVerify { repository.disableTask(task) }
        confirmVerified(repository)
    }

    @Test
    fun `should call deleteAlarm when disable task`() = runBlocking {
        disableTask(task)

        coVerify { deleteAlarmUseCase(task.id) }
        confirmVerified(deleteAlarmUseCase)
    }

}