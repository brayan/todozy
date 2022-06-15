package br.com.sailboat.todozy.feature.task.list.impl.presentation.factory

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.feature.task.list.impl.presentation.mapper.TaskToTaskUiModelMapper
import br.com.sailboat.todozy.feature.task.list.impl.presentation.mapper.TaskCategoryToStringMapper
import br.com.sailboat.uicomponent.model.SubheadUiModel
import br.com.sailboat.uicomponent.model.TaskUiModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class TaskListUiModelFactoryImplTest {

    private val taskToTaskUiModelMapper: TaskToTaskUiModelMapper = mockk(relaxed = true)
    private val taskCategoryToStringMapper: TaskCategoryToStringMapper = mockk(relaxed = true)

    private val taskListUiModelFactory = TaskListUiModelFactoryImpl(
        taskToTaskUiModelMapper = taskToTaskUiModelMapper,
        taskCategoryToStringMapper = taskCategoryToStringMapper,
    )

    @Test
    fun `should create an subhead and a task when create is called from tasksUiModelFactory`() = runBlocking {
        val taskList = listOf(
            Task(
                id = 42L,
                name = "Task Name",
                notes = "Notes",
            )
        )
        val taskUiModel = TaskUiModel(
            taskId = 42L,
            taskName = "Task Name",
        )
        prepareScenario(
            taskListUiModel = listOf(taskUiModel),
            subhead = "Today",
        )

        val result = taskListUiModelFactory.create(taskList, TaskCategory.TODAY)

        val expected = listOf(
            SubheadUiModel("Today"),
            taskUiModel,
        )
        assertEquals(expected, result)
    }

    @Test
    fun `should not create an subhead and a task when task list is empty`() = runBlocking {
        prepareScenario()

        val result = taskListUiModelFactory.create(emptyList(), TaskCategory.TODAY)

        assertEquals(emptyList(), result)
    }

    private fun prepareScenario(
        taskListUiModel: List<TaskUiModel> = emptyList(),
        subhead: String = "Today",
    ) {
        coEvery { taskToTaskUiModelMapper.map(any()) } returns taskListUiModel
        coEvery { taskCategoryToStringMapper.map(any()) } returns subhead
    }
}