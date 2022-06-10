package br.com.sailboat.todozy.feature.task.list.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.history.domain.usecase.AddHistoryUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetNextAlarmUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.form.domain.usecase.SaveTaskUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.*

class CompleteTaskTest {

    private val getTaskUseCase: GetTaskUseCase = mockk(relaxed = true)
    private val getNextAlarmUseCase: GetNextAlarmUseCase = mockk(relaxed = true)
    private val saveTaskUseCase: SaveTaskUseCase = mockk(relaxed = true)
    private val disableTaskUseCase: DisableTaskUseCase = mockk(relaxed = true)
    private val addHistoryUseCase: br.com.sailboat.todozy.feature.task.history.domain.usecase.AddHistoryUseCase = mockk(relaxed = true)

    private val completeTask = CompleteTask(
        getTaskUseCase = getTaskUseCase,
        getNextAlarmUseCase = getNextAlarmUseCase,
        saveTaskUseCase = saveTaskUseCase,
        disableTaskUseCase = disableTaskUseCase,
        addHistoryUseCase = addHistoryUseCase,
    )

    @Test
    fun `should get task from id`() = runBlocking {
        val taskId = 42L
        val task = Task(
            id = taskId,
            name = "Task Name",
            notes = "Some notes",
        )
        prepareScenario(taskResult = Result.success(task))

        completeTask(taskId, TaskStatus.DONE)

        coVerify { getTaskUseCase(taskId) }
        confirmVerified(getTaskUseCase)
    }

    @Test
    fun `should disable task when alarm is non-repetitive`() = runBlocking {
        val taskId = 42L
        val task = Task(
            id = taskId,
            name = "Task Name",
            notes = "Some notes",
        )
        prepareScenario(taskResult = Result.success(task))

        completeTask(taskId, TaskStatus.DONE)

        coVerify { disableTaskUseCase(task) }
        confirmVerified(disableTaskUseCase)
    }

    @Test
    fun `should disable task when alarm is null`() = runBlocking {
        val taskId = 42L
        val task = Task(
            id = taskId,
            name = "Task Name",
            notes = "Some notes",
        )
        prepareScenario(taskResult = Result.success(task))

        completeTask(taskId, TaskStatus.DONE)

        coVerify { disableTaskUseCase(task) }
        confirmVerified(disableTaskUseCase)
    }

    @Test
    fun `should get next alarm and save task when alarm is repetitive`() = runBlocking {
        val alarm = Alarm(
            dateTime = Calendar.getInstance(),
            repeatType = RepeatType.DAY
        )
        val taskId = 42L
        val task = Task(
            id = taskId,
            name = "Task Name",
            notes = "Some notes",
            alarm = alarm,
        )
        prepareScenario(
            taskResult = Result.success(task),
            alarmResult = alarm,
        )

        completeTask(taskId, TaskStatus.DONE)

        coVerify { getNextAlarmUseCase(alarm) }
        coVerify { saveTaskUseCase(task) }
        confirmVerified(saveTaskUseCase)
        confirmVerified(getNextAlarmUseCase)
    }

    @Test
    fun `should add history after complete task`() = runBlocking {
        val task = Task(
            id = 42L,
            name = "Task Name",
            notes = "Some notes",
        )
        prepareScenario(taskResult = Result.success(task))

        completeTask(45, TaskStatus.DONE)

        coVerify { addHistoryUseCase(task, TaskStatus.DONE) }
        confirmVerified(addHistoryUseCase)
    }

    private fun prepareScenario(
        taskResult: Result<Task> = Result.success(
            Task(
                id = 42L,
                name = "Task Name",
                notes = "Some notes",
            )
        ),
        alarmResult: Alarm = Alarm(
            dateTime = Calendar.getInstance(),
            repeatType = RepeatType.DAY
        )
    ) {
        coEvery { getTaskUseCase(any()) } returns taskResult
        coEvery { getNextAlarmUseCase(any()) } returns alarmResult
        coEvery { disableTaskUseCase(any()) } returns taskResult
        coEvery { saveTaskUseCase(any()) } returns taskResult
    }

}