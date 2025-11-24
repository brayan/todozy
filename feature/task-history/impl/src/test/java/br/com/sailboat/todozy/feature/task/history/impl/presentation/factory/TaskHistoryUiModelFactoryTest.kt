package br.com.sailboat.todozy.feature.task.history.impl.presentation.factory

import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistory
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryCategory
import br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper.TaskHistoryCategoryToStringMapper
import br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper.TaskHistoryToTaskHistoryUiModelMapper
import br.com.sailboat.uicomponent.model.SubheadUiModel
import br.com.sailboat.uicomponent.model.TaskHistoryUiModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

internal class TaskHistoryUiModelFactoryTest {
    private val taskHistoryCategoryToStringMapper: TaskHistoryCategoryToStringMapper = mockk(relaxed = true)
    private val taskHistoryToTaskHistoryUiModelMapper: TaskHistoryToTaskHistoryUiModelMapper = mockk(relaxed = true)

    private val taskHistoryUiModelFactory =
        TaskHistoryUiModelFactory(
            taskHistoryCategoryToStringMapper = taskHistoryCategoryToStringMapper,
            taskHistoryToTaskHistoryUiModelMapper = taskHistoryToTaskHistoryUiModelMapper,
        )

    @Test
    fun `should create a subhead and a task history when create is called from taskHistoryUiModelFactory`() =
        runBlocking {
            val taskHistoryList =
                listOf(
                    TaskHistory(
                        id = 42L,
                        taskId = 48L,
                        taskName = "Task Name",
                        status = TaskStatus.DONE,
                        insertingDate = "2022-06-22-19-47-38",
                    ),
                )
            val taskUiModel =
                TaskHistoryUiModel(
                    id = 42L,
                    taskName = "Task Name",
                    done = true,
                    insertingDate = "2022-06-22-19-47-38",
                )
            prepareScenario(
                taskHistoryListUiModel = listOf(taskUiModel),
                subhead = "Today",
            )

            val result = taskHistoryUiModelFactory.create(taskHistoryList, TaskHistoryCategory.TODAY)

            val expected =
                listOf(
                    SubheadUiModel("Today"),
                    taskUiModel,
                )
            assertEquals(expected, result)
        }

    @Test
    fun `should not create a subhead and a task when task list is empty`() =
        runBlocking {
            prepareScenario()

            val result = taskHistoryUiModelFactory.create(emptyList(), TaskHistoryCategory.TODAY)

            assertEquals(emptyList(), result)
        }

    private fun prepareScenario(
        taskHistoryListUiModel: List<TaskHistoryUiModel> = emptyList(),
        subhead: String = "Today",
    ) {
        coEvery { taskHistoryToTaskHistoryUiModelMapper.map(any()) } returns taskHistoryListUiModel
        coEvery { taskHistoryCategoryToStringMapper.map(any()) } returns subhead
    }
}
