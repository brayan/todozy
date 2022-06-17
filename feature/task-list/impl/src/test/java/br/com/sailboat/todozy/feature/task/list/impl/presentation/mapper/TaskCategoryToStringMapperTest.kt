package br.com.sailboat.todozy.feature.task.list.impl.presentation.mapper

import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.utility.android.string.StringProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class TaskCategoryToStringMapperTest {

    private val stringProvider: StringProvider = mockk(relaxed = true)

    private val taskCategoryToStringMapper = TaskCategoryToStringMapper(
        stringProvider = stringProvider
    )

    @Test
    fun `should map String from task category BEFORE_TODAY`() = runBlocking {
        val taskCategoryString = "Before today"
        prepareScenario(taskCategoryString = taskCategoryString)

        val result = taskCategoryToStringMapper.map(TaskCategory.BEFORE_TODAY)

        assertEquals(taskCategoryString, result)
    }

    @Test
    fun `should map String from task category TODAY`() = runBlocking {
        val taskCategoryString = "Today"
        prepareScenario(taskCategoryString = taskCategoryString)

        val result = taskCategoryToStringMapper.map(TaskCategory.TODAY)

        assertEquals(taskCategoryString, result)
    }

    @Test
    fun `should map String from task category TOMORROW`() = runBlocking {
        val taskCategoryString = "Tomorrow"
        prepareScenario(taskCategoryString = taskCategoryString)

        val result = taskCategoryToStringMapper.map(TaskCategory.TOMORROW)

        assertEquals(taskCategoryString, result)
    }

    @Test
    fun `should map String from task category NEXT_DAYS`() = runBlocking {
        val taskCategoryString = "Next days"
        prepareScenario(taskCategoryString = taskCategoryString)

        val result = taskCategoryToStringMapper.map(TaskCategory.NEXT_DAYS)

        assertEquals(taskCategoryString, result)
    }

    private fun prepareScenario(taskCategoryString: String = "Today") {
        coEvery { stringProvider.getString(any()) } returns taskCategoryString
    }
}
