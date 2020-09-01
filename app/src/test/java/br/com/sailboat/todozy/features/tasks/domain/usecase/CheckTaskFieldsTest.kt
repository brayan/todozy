package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class CheckTaskFieldsTest {

    private lateinit var checkTaskFields: CheckTaskFields

    @Before
    fun setUp() {
        checkTaskFields = CheckTaskFields()
    }

    @Test
    fun `should get TASK_NAME_NOT_FILLED when task name is empty`() = runBlocking {
        val task = Task(id = Entity.NO_ID, name = "", notes = "Some notes")

        val result = checkTaskFields(task)

        assertEquals(listOf(CheckTaskFields.Condition.TASK_NAME_NOT_FILLED), result)
    }

    @Test
    fun `should get ALARM_NOT_VALID when task alarm is before now`() = runBlocking {
        val task = Task(id = Entity.NO_ID, name = "Task", notes = "Some notes",
                alarm = Alarm(
                        dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) },
                        repeatType = RepeatType.NOT_REPEAT
                ))

        val result = checkTaskFields(task)

        assertEquals(listOf(CheckTaskFields.Condition.ALARM_NOT_VALID), result)
    }

    @Test
    fun `should get TASK_NAME_NOT_FILLED and ALARM_NOT_VALID when task alarm is before now`() = runBlocking {
        val task = Task(id = Entity.NO_ID, name = "", notes = "Some notes",
                alarm = Alarm(
                        dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) },
                        repeatType = RepeatType.NOT_REPEAT
                ))

        val result = checkTaskFields(task)

        assertEquals(listOf(
                CheckTaskFields.Condition.TASK_NAME_NOT_FILLED,
                CheckTaskFields.Condition.ALARM_NOT_VALID), result)
    }

}