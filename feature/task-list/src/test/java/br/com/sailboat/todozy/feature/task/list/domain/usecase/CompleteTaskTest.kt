package br.com.sailboat.todozy.feature.task.list.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.domain.usecase.SaveTaskUseCase
import br.com.sailboat.todozy.domain.usecase.GetNextAlarmUseCase
import br.com.sailboat.todozy.domain.usecase.AddHistoryUseCase
import br.com.sailboat.todozy.feature.task.list.domain.usecase.CompleteTask
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
    private val addHistoryUseCase: AddHistoryUseCase = mockk(relaxed = true)

    private val completeTask = CompleteTask(
        getTaskUseCase = getTaskUseCase,
        getNextAlarmUseCase = getNextAlarmUseCase,
        saveTaskUseCase = saveTaskUseCase,
        disableTaskUseCase = disableTaskUseCase,
        addHistoryUseCase = addHistoryUseCase,
    )

    @Test
    fun `should get task from id`() = runBlocking {
        val task = Task(
            id = 45, name = "Task Name", notes = "Some notes", alarm = Alarm(
                dateTime = Calendar.getInstance(),
                repeatType = RepeatType.NOT_REPEAT
            )
        )
        coEvery { getTaskUseCase(45) } returns task

        completeTask(45, TaskStatus.DONE)

        coVerify { getTaskUseCase(45) }
        confirmVerified(getTaskUseCase)
    }

    @Test
    fun `should disable task when alarm is non-repetitive`() = runBlocking {
        val task = Task(
            id = 45, name = "Task Name", notes = "Some notes", alarm = Alarm(
                dateTime = Calendar.getInstance(),
                repeatType = RepeatType.NOT_REPEAT
            )
        )
        coEvery { getTaskUseCase(45) } returns task

        completeTask(45, TaskStatus.DONE)

        coVerify { disableTaskUseCase(task) }
        confirmVerified(disableTaskUseCase)
    }

    @Test
    fun `should disable task when alarm is null`() = runBlocking {
        val task = Task(id = 45, name = "Task Name", notes = "Some notes", alarm = null)
        coEvery { getTaskUseCase(any()) } returns task

        completeTask(45, TaskStatus.DONE)

        coVerify { disableTaskUseCase(task) }
        confirmVerified(disableTaskUseCase)
    }

    @Test
    fun `should get next alarm and save task when alarm is repetitive`() = runBlocking {
        val dateTime = Calendar.getInstance()
        val alarm = Alarm(
            dateTime = dateTime,
            repeatType = RepeatType.DAY
        )
        val task = Task(id = 45, name = "Task Name", notes = "Some notes", alarm = alarm)
        coEvery { getTaskUseCase(45) } returns task
        coEvery { getNextAlarmUseCase(any()) } returns alarm

        completeTask(45, TaskStatus.DONE)

        coVerify { getNextAlarmUseCase(alarm) }
        coVerify { saveTaskUseCase(task) }
        confirmVerified(saveTaskUseCase)
        confirmVerified(getNextAlarmUseCase)
    }

    @Test
    fun `should add history after complete task`() = runBlocking {
        val dateTime = Calendar.getInstance()
        val alarm = Alarm(
            dateTime = dateTime,
            repeatType = RepeatType.DAY
        )
        val task = Task(id = 45, name = "Task Name", notes = "Some notes", alarm = alarm)
        coEvery { getTaskUseCase(45) } returns task
        coEvery { getNextAlarmUseCase(any()) } returns alarm

        completeTask(45, TaskStatus.DONE)

        coVerify { addHistoryUseCase(task, TaskStatus.DONE) }
        confirmVerified(addHistoryUseCase)
    }

}