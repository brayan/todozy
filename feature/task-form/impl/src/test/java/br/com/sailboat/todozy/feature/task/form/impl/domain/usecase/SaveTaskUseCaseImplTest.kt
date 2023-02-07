package br.com.sailboat.todozy.feature.task.form.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.mock.TaskMockFactory
import br.com.sailboat.todozy.domain.repository.TaskRepository
import br.com.sailboat.todozy.feature.alarm.domain.usecase.DeleteAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.SaveAlarmUseCase
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.Calendar

internal class SaveTaskUseCaseImplTest {

    private val repository: TaskRepository = mockk(relaxed = true)
    private val deleteAlarmUseCase: DeleteAlarmUseCase = mockk(relaxed = true)
    private val saveAlarmUseCase: SaveAlarmUseCase = mockk(relaxed = true)
    private val checkTaskFieldsUseCase: CheckTaskFieldsUseCase = mockk(relaxed = true)

    private val saveTaskUseCase = SaveTaskUseCaseImpl(
        taskRepository = repository,
        deleteAlarmUseCase = deleteAlarmUseCase,
        saveAlarmUseCase = saveAlarmUseCase,
        checkTaskFieldsUseCase = checkTaskFieldsUseCase,
    )

    @Test
    fun `should insert task in the repository when task has no id`() = runBlocking {
        val task = Task(id = Entity.NO_ID, name = "Task Name", notes = "Some notes")
        prepareScenario()

        saveTaskUseCase(task)

        coVerify(exactly = 1) { repository.insert(task) }
        coVerify(exactly = 0) { repository.update(task) }
        confirmVerified(repository)
    }

    @Test
    fun `should update task in the repository when task has some id`() = runBlocking {
        prepareScenario()
        val task = Task(id = 45, name = "Task Name", notes = "Some notes")

        saveTaskUseCase(task)

        coVerify(exactly = 0) { repository.insert(task) }
        coVerify(exactly = 1) { repository.update(task) }
        confirmVerified(repository)
    }

    @Test
    fun `should check task fields when save task is called`() = runBlocking {
        prepareScenario()
        val task = Task(id = 45, name = "Task Name", notes = "Some notes")

        saveTaskUseCase(task)

        coVerify(exactly = 1) { checkTaskFieldsUseCase(task) }
    }

    @Test
    fun `should delete alarm when update task`() = runBlocking {
        prepareScenario()
        val alarm = Alarm(
            dateTime = Calendar.getInstance().apply { add(Calendar.DATE, 1) },
            repeatType = RepeatType.NOT_REPEAT
        )
        val task = Task(id = 45, name = "Task Name", notes = "Some notes", alarm = alarm)

        saveTaskUseCase(task)

        coVerify { deleteAlarmUseCase(45) }
        confirmVerified(deleteAlarmUseCase)
    }

    @Test
    fun `should save alarm when inserting task`() = runBlocking {
        val alarm = Alarm(
            dateTime = Calendar.getInstance().apply { add(Calendar.DATE, 1) },
            repeatType = RepeatType.NOT_REPEAT
        )
        val task = Task(
            id = Entity.NO_ID,
            name = "Task Name",
            notes = "Some notes",
            alarm = alarm,
        )
        prepareScenario(insertTaskResult = Result.success(task))

        saveTaskUseCase(task)

        coVerify { saveAlarmUseCase(alarm, task.id) }
        confirmVerified(saveAlarmUseCase)
    }

    @Test
    fun `should save alarm when updating task`() = runBlocking {
        prepareScenario()
        val alarm = Alarm(
            dateTime = Calendar.getInstance().apply { add(Calendar.DATE, 1) },
            repeatType = RepeatType.NOT_REPEAT
        )
        val task = Task(id = 45, name = "Task Name", notes = "Some notes", alarm = alarm)

        saveTaskUseCase(task)

        coVerify { saveAlarmUseCase(alarm, task.id) }
        confirmVerified(saveAlarmUseCase)
    }

    private fun prepareScenario(
        insertTaskResult: Result<Task> = Result.success(TaskMockFactory.makeTask()),
        updateTaskResult: Result<Task> = Result.success(TaskMockFactory.makeTask()),
    ) {
        coEvery { repository.insert(any()) } returns insertTaskResult
        coEvery { repository.update(any()) } returns updateTaskResult
    }
}
