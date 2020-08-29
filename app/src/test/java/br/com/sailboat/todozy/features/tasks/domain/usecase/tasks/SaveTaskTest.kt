package br.com.sailboat.todozy.features.tasks.domain.usecase.tasks

import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class SaveTaskTest {

    private val repository: TaskRepository = mockk(relaxed = true)

    private lateinit var saveTask: SaveTask

    @Before
    fun setUp() {
        saveTask = SaveTask(repository)
    }

    @Test
    fun `should insert task in the repository when task has no id`() = runBlocking {
        val task = Task(id = Entity.NO_ID, name = "Task Name", notes = "Some notes")

        saveTask(task)

        coVerify(exactly = 1) { repository.insert(task) }
        coVerify(exactly = 0) { repository.update(task) }
        confirmVerified(repository)
    }

    @Test
    fun `should update task in the repository when task has some id`() = runBlocking {
        val task = Task(id = 45, name = "Task Name", notes = "Some notes")

        saveTask(task)

        coVerify(exactly = 0) { repository.insert(task) }
        coVerify(exactly = 1) { repository.update(task) }
        confirmVerified(repository)
    }

    @Test(expected = CheckTaskFields.TaskFieldsException::class)
    fun `should throw TaskFieldsException when task name is empty`() = runBlocking {
        val task = Task(id = 45, name = "", notes = "Some notes")

        saveTask(task)
    }

    @Test(expected = CheckTaskFields.TaskFieldsException::class)
    fun `should throw TaskFieldsException when task alarm is before now`() = runBlocking {
        val task = Task(id = 45, name = "Task Name", notes = "Some notes", alarm = Alarm(
                dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) },
                repeatType = RepeatType.NOT_REPEAT
        ))

        saveTask(task)
    }

}