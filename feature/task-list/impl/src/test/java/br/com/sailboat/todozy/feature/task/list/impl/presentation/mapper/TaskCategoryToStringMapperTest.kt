package br.com.sailboat.todozy.feature.task.list.impl.presentation.mapper

import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.utility.android.string.StringProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import br.com.sailboat.todozy.feature.task.list.impl.R

class TaskCategoryToStringMapperTest {

    private val stringProvider: StringProvider = mockk(relaxed = true)

    private val taskCategoryToStringMapper = TaskCategoryToStringMapper(
        stringProvider = stringProvider
    )

    @Test
    fun `should map String from task category BEFORE_TODAY`() = runBlocking {
        val taskCategoryInt = 2132017400
        val taskCategoryString = "Before today"
        coEvery { stringProvider.getString(taskCategoryInt) } returns taskCategoryString

        val result = taskCategoryToStringMapper.map(TaskCategory.BEFORE_TODAY)

        assertEquals(taskCategoryString, result)
    }

    @Test
    fun `should map String from task category TODAY`() = runBlocking {
        val taskCategoryInt = 2132017430
        val taskCategoryString = "Today"
        coEvery { stringProvider.getString(taskCategoryInt) } returns taskCategoryString

        val result = taskCategoryToStringMapper.map(TaskCategory.TODAY)

        assertEquals(taskCategoryString, result)
    }

    @Test
    fun `should map String from task category TOMORROW`() = runBlocking {
        val taskCategoryInt = 2132017431
        val taskCategoryString = "Tomorrow"
        coEvery { stringProvider.getString(taskCategoryInt) } returns taskCategoryString

        val result = taskCategoryToStringMapper.map(TaskCategory.TOMORROW)

        assertEquals(taskCategoryString, result)
    }

    @Test
    fun `should map String from task category NEXT_DAYS`() = runBlocking {
        val taskCategoryInt = 2132017380
        val taskCategoryString = "Next days"
        coEvery { stringProvider.getString(taskCategoryInt) } returns taskCategoryString

        val result = taskCategoryToStringMapper.map(TaskCategory.NEXT_DAYS)

        assertEquals(taskCategoryString, result)
    }
}
