package br.com.sailboat.todozy.feature.task.form.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskFieldsConditions
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertEquals

internal class CheckTaskFieldsUseCaseImplTest {
    private val checkTaskFieldsUseCase = CheckTaskFieldsUseCaseImpl()

    @Test
    fun `should get TASK_NAME_NOT_FILLED when task name is empty`() =
        runBlocking {
            val task = Task(id = Entity.NO_ID, name = "", notes = "Some notes")

            val result = checkTaskFieldsUseCase(task)

            val expected = listOf(TaskFieldsConditions.TASK_NAME_NOT_FILLED)
            assertEquals(expected, result)
        }

    @Test
    fun `should get ALARM_NOT_VALID when task alarm is before now`() =
        runBlocking {
            val task =
                Task(
                    id = Entity.NO_ID,
                    name = "Task",
                    notes = "Some notes",
                    alarm =
                        Alarm(
                            dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) },
                            repeatType = RepeatType.NOT_REPEAT,
                        ),
                )

            val result = checkTaskFieldsUseCase(task)

            val expected = listOf(TaskFieldsConditions.ALARM_NOT_VALID)
            assertEquals(expected, result)
        }

    @Test
    fun `should get TASK_NAME_NOT_FILLED and ALARM_NOT_VALID when task alarm is before now`() =
        runBlocking {
            val task =
                Task(
                    id = Entity.NO_ID,
                    name = "",
                    notes = "Some notes",
                    alarm =
                        Alarm(
                            dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) },
                            repeatType = RepeatType.NOT_REPEAT,
                        ),
                )

            val result = checkTaskFieldsUseCase(task)

            val expected =
                listOf(
                    TaskFieldsConditions.TASK_NAME_NOT_FILLED,
                    TaskFieldsConditions.ALARM_NOT_VALID,
                )
            assertEquals(expected, result)
        }
}
