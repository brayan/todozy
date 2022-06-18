package br.com.sailboat.todozy.feature.task.details.impl.presentation.factory

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.feature.alarm.presentation.mapper.AlarmToAlarmUiModelMapper
import br.com.sailboat.todozy.utility.android.string.StringProvider
import br.com.sailboat.uicomponent.model.AlarmUiModel
import br.com.sailboat.uicomponent.model.LabelUiModel
import br.com.sailboat.uicomponent.model.LabelValueUiModel
import br.com.sailboat.uicomponent.model.SubheadUiModel
import br.com.sailboat.uicomponent.model.TaskUiModel
import br.com.sailboat.uicomponent.model.TitleUiModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.Calendar
import kotlin.test.assertEquals

class TaskDetailsUiModelFactoryTest {

    private val stringProvider: StringProvider = mockk(relaxed = true)
    private val alarmToAlarmUiModelMapper: AlarmToAlarmUiModelMapper = mockk(relaxed = true)

    private val taskDetailsUiModelFactory = TaskDetailsUiModelFactory(
        stringProvider = stringProvider,
        alarmToAlarmUiModelMapper = alarmToAlarmUiModelMapper,
    )

    @Test
    fun `should create task details list when create is called from taskDetailsUiModelFactory`() = runBlocking {
        val task = Task(
            id = 45L,
            name = "Task Name",
            notes = "Task Notes",
            alarm = Alarm(
                dateTime = Calendar.getInstance(),
                repeatType = RepeatType.WEEK,
            ),
        )
        val alarmUiModel = AlarmUiModel(
            date = "07/03/2022",
            time = "11:55",
            description = "Today, March 7, 2022",
            isCustom = false,
            shouldRepeat = true,
            customDays = null,
        )
        prepareScenario(
            stringProviderResult = "Title",
            alarmUiModel = alarmUiModel,
        )

        val result = taskDetailsUiModelFactory.create(task)

        val expected = listOf(
            TitleUiModel("Task Name"),
            LabelUiModel("Title"),
            alarmUiModel,
            LabelValueUiModel(
                label = "Title",
                value = "Task Notes"
            ),
        )
        assertEquals(expected, result)
    }

    private fun prepareScenario(
        stringProviderResult: String = "Title",
        alarmUiModel: AlarmUiModel = AlarmUiModel(
            date = "07/03/2022",
            time = "11:55",
            description = "Today, March 7, 2022",
            isCustom = false,
            shouldRepeat = true,
            customDays = null,
        ),
    ) {
        coEvery { stringProvider.getString(any()) } returns stringProviderResult
        coEvery { alarmToAlarmUiModelMapper.map(any()) } returns alarmUiModel
    }
}
