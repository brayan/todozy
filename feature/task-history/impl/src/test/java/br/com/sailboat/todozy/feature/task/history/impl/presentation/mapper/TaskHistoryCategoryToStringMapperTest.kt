package br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper

import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.utility.kotlin.StringProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

internal class TaskHistoryCategoryToStringMapperTest {
    private val stringProvider: StringProvider = mockk(relaxed = true)

    private val taskHistoryCategoryToStringMapper =
        TaskHistoryCategoryToStringMapper(
            stringProvider = stringProvider,
        )

    @Test
    fun `should map String from task history category TODAY`() = runBlocking {
        val taskHistoryCategoryString = "Today"
        prepareScenario(taskHistoryCategoryString = taskHistoryCategoryString)

        val result = taskHistoryCategoryToStringMapper.map(TaskHistoryCategory.TODAY)

        assertEquals(taskHistoryCategoryString, result)
    }

    @Test
    fun `should map String from task history category YESTERDAY`() = runBlocking {
        val taskHistoryCategoryString = "Yesterday"
        prepareScenario(taskHistoryCategoryString = taskHistoryCategoryString)

        val result = taskHistoryCategoryToStringMapper.map(TaskHistoryCategory.YESTERDAY)

        assertEquals(taskHistoryCategoryString, result)
    }

    @Test
    fun `should map String from task history category PREVIOUS_DAYS`() = runBlocking {
        val taskHistoryCategoryString = "Previous days"
        prepareScenario(taskHistoryCategoryString = taskHistoryCategoryString)

        val result = taskHistoryCategoryToStringMapper.map(TaskHistoryCategory.PREVIOUS_DAYS)

        assertEquals(taskHistoryCategoryString, result)
    }

    private fun prepareScenario(taskHistoryCategoryString: String = "Today") {
        coEvery { stringProvider.getString(any()) } returns taskHistoryCategoryString
    }
}
