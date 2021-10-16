package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.DeleteAlarmUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.SaveAlarmUseCase
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*

class SaveTaskTest {

    private val repository: TaskRepository = mockk(relaxed = true)
    private val deleteAlarmUseCase: DeleteAlarmUseCase = mockk(relaxed = true)
    private val saveAlarmUseCase: SaveAlarmUseCase = mockk(relaxed = true)
    private val checkTaskFieldsUseCase: CheckTaskFieldsUseCase = mockk(relaxed = true)

    private val saveTask = SaveTask(
        taskRepository = repository,
        deleteAlarmUseCase = deleteAlarmUseCase,
        saveAlarmUseCase = saveAlarmUseCase,
        checkTaskFieldsUseCase = checkTaskFieldsUseCase,
    )

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

    @Test
    fun `should check task fields when save task is called`() = runBlocking {
        val task = Task(id = 45, name = "Task Name", notes = "Some notes")

        saveTask(task)

        coVerify(exactly = 1) { checkTaskFieldsUseCase(task) }
    }

    @Test
    fun `should delete alarm when update task`() = runBlocking {
        val alarm = Alarm(
            dateTime = Calendar.getInstance().apply { add(Calendar.DATE, 1) },
            repeatType = RepeatType.NOT_REPEAT
        )
        val task = Task(id = 45, name = "Task Name", notes = "Some notes", alarm = alarm)

        saveTask(task)

        coVerify { deleteAlarmUseCase(45) }
        confirmVerified(deleteAlarmUseCase)
    }

    @Test
    fun `should save alarm when inserting task`() = runBlocking {
        val alarm = Alarm(
            dateTime = Calendar.getInstance().apply { add(Calendar.DATE, 1) },
            repeatType = RepeatType.NOT_REPEAT
        )
        val task = Task(id = Entity.NO_ID, name = "Task Name", notes = "Some notes", alarm = alarm)

        saveTask(task)

        coVerify { saveAlarmUseCase(alarm, task.id) }
        confirmVerified(saveAlarmUseCase)
    }

    @Test
    fun `should save alarm when updating task`() = runBlocking {
        val alarm = Alarm(
            dateTime = Calendar.getInstance().apply { add(Calendar.DATE, 1) },
            repeatType = RepeatType.NOT_REPEAT
        )
        val task = Task(id = 45, name = "Task Name", notes = "Some notes", alarm = alarm)

        saveTask(task)

        coVerify { saveAlarmUseCase(alarm, task.id) }
        confirmVerified(saveAlarmUseCase)
    }

}