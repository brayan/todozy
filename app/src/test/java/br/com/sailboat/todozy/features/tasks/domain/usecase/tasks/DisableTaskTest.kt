package br.com.sailboat.todozy.features.tasks.domain.usecase.tasks

import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.DeleteAlarm
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DisableTaskTest {

    private val repository: TaskRepository = mockk(relaxed = true)
    private val deleteAlarm: DeleteAlarm = mockk(relaxed = true)

    private lateinit var disableTask: DisableTask

    private val task = Task(id = Entity.NO_ID, name = "Task Name", notes = "Some notes")

    @Before
    fun setUp() {
        disableTask = DisableTask(repository, deleteAlarm)
    }

    @Test
    fun `should disable task from repository`() = runBlocking {
        disableTask(task)

        coVerify { repository.disableTask(task) }
        confirmVerified(repository)
    }

    @Test
    fun `should call deleteAlarm when disable task`() = runBlocking {
        disableTask(task)

        coVerify { deleteAlarm(task) }
        confirmVerified(deleteAlarm)
    }

}