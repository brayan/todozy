package br.com.sailboat.todozy.feature.task.history.impl.data.repository

import br.com.sailboat.todozy.domain.model.TaskHistory
import br.com.sailboat.todozy.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.model.mock.TaskMockFactory.makeTask
import br.com.sailboat.todozy.feature.task.history.impl.data.datasource.TaskHistoryLocalDataSource
import br.com.sailboat.todozy.feature.task.history.impl.data.model.TaskHistoryData
import br.com.sailboat.todozy.feature.task.history.impl.data.repository.TaskHistoryDataMockFactory.makeTaskHistoryData
import br.com.sailboat.todozy.feature.task.history.impl.data.repository.TaskHistoryDataMockFactory.makeTaskHistoryDataList
import br.com.sailboat.todozy.feature.task.history.impl.domain.factory.TaskHistoryMockFactory.makeTaskHistory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class TaskHistoryRepositoryImplTest {

    private val taskHistoryLocalDataSource: TaskHistoryLocalDataSource = mockk(relaxed = true)

    private val taskHistoryRepository = TaskHistoryRepositoryImpl(
        taskHistoryLocalDataSource = taskHistoryLocalDataSource,
    )

    @Test
    fun `should call getTotalOfNotDoneTasks from local data source when getTotalOfNotDoneTasks is called from repository`() =
        runBlocking {
            val taskHistoryFilter = TaskHistoryFilter()
            val totalOfNotDoneTasks = 25
            prepareScenario(totalOfNotDoneTasks = totalOfNotDoneTasks)

            val result = taskHistoryRepository.getTotalOfNotDoneTasks(taskHistoryFilter)

            assertEquals(totalOfNotDoneTasks, result)
            coVerify { taskHistoryLocalDataSource.getTotalOfNotDoneTasks(taskHistoryFilter) }
        }

    @Test
    fun `should call getTotalOfDoneTasks from local data source when getTotalOfDoneTasks is called from repository`() =
        runBlocking {
            val taskHistoryFilter = TaskHistoryFilter()
            val totalOfDoneTasks = 25
            prepareScenario(totalOfDoneTasks = totalOfDoneTasks)

            val result = taskHistoryRepository.getTotalOfDoneTasks(taskHistoryFilter)

            assertEquals(totalOfDoneTasks, result)
            coVerify { taskHistoryLocalDataSource.getTotalOfDoneTasks(taskHistoryFilter) }
        }

    @Test
    fun `should call getTodayHistory from local data source when getTodayHistory is called from repository`() =
        runBlocking {
            val taskHistoryData = makeTaskHistoryData()
            val taskHistoryFilter = TaskHistoryFilter()
            prepareScenario(taskHistoryDataList = listOf(taskHistoryData))

            val result = taskHistoryRepository.getTodayHistory(taskHistoryFilter)

            val expected = listOf(
                TaskHistory(
                    id = taskHistoryData.id,
                    taskId = taskHistoryData.taskId,
                    taskName = taskHistoryData.taskName.orEmpty(),
                    status = TaskStatus.DONE,
                    insertingDate = taskHistoryData.insertingDate.orEmpty(),
                )
            )
            assertEquals(expected, result)
            coVerify { taskHistoryLocalDataSource.getTodayHistory(taskHistoryFilter) }
        }

    @Test
    fun `should call getYesterdayHistory from local data source when getYesterdayHistory is called from repository`() =
        runBlocking {
            val taskHistoryData = makeTaskHistoryData()
            val taskHistoryFilter = TaskHistoryFilter()
            prepareScenario(taskHistoryDataList = listOf(taskHistoryData))

            val result = taskHistoryRepository.getYesterdayHistory(taskHistoryFilter)

            val expected = listOf(
                TaskHistory(
                    id = taskHistoryData.id,
                    taskId = taskHistoryData.taskId,
                    taskName = taskHistoryData.taskName.orEmpty(),
                    status = TaskStatus.DONE,
                    insertingDate = taskHistoryData.insertingDate.orEmpty(),
                )
            )
            assertEquals(expected, result)
            coVerify { taskHistoryLocalDataSource.getYesterdayHistory(taskHistoryFilter) }
        }

    @Test
    fun `should call getPreviousDaysHistory from local data source when getPreviousDaysHistory is called from repository`() =
        runBlocking {
            val taskHistoryData = makeTaskHistoryData()
            val taskHistoryFilter = TaskHistoryFilter()
            prepareScenario(taskHistoryDataList = listOf(taskHistoryData))

            val result = taskHistoryRepository.getPreviousDaysHistory(taskHistoryFilter)

            val expected = listOf(
                TaskHistory(
                    id = taskHistoryData.id,
                    taskId = taskHistoryData.taskId,
                    taskName = taskHistoryData.taskName.orEmpty(),
                    status = TaskStatus.DONE,
                    insertingDate = taskHistoryData.insertingDate.orEmpty(),
                )
            )
            assertEquals(expected, result)
            coVerify { taskHistoryLocalDataSource.getPreviousDaysHistory(taskHistoryFilter) }
        }

    @Test
    fun `should call getTaskHistoryByTask from local data source when getTaskHistory is called from repository`() =
        runBlocking {
            val taskHistoryData = makeTaskHistoryData()
            prepareScenario(taskHistoryDataList = listOf(taskHistoryData))

            val result = taskHistoryRepository.getTaskHistory(taskHistoryData.taskId)

            val expected = listOf(
                TaskHistory(
                    id = taskHistoryData.id,
                    taskId = taskHistoryData.taskId,
                    taskName = taskHistoryData.taskName.orEmpty(),
                    status = TaskStatus.DONE,
                    insertingDate = taskHistoryData.insertingDate.orEmpty(),
                )
            )
            assertEquals(expected, result)
            coVerify { taskHistoryLocalDataSource.getTaskHistoryByTask(taskHistoryData.taskId) }
        }

    @Test
    fun `should call save from local data source when insert is called from repository`() =
        runBlocking {
            val task = makeTask()
            prepareScenario()

            taskHistoryRepository.insert(task, TaskStatus.DONE)

            coVerify { taskHistoryLocalDataSource.save(task.id, 1) }
        }

    @Test
    fun `should call update from local data source when update is called from repository`() =
        runBlocking {
            val taskHistory = makeTaskHistory()
            val taskHistoryData = TaskHistoryData(
                id = taskHistory.id,
                taskId = taskHistory.taskId,
                taskName = taskHistory.taskName,
                status = 1,
                insertingDate = taskHistory.insertingDate,
                enabled = true,
            )
            prepareScenario()

            taskHistoryRepository.update(taskHistory)

            coVerify { taskHistoryLocalDataSource.update(taskHistoryData) }
        }

    @Test
    fun `should call delete from local data source when delete is called from repository`() =
        runBlocking {
            val taskHistory = makeTaskHistory()
            prepareScenario()

            taskHistoryRepository.delete(taskHistory)

            coVerify { taskHistoryLocalDataSource.delete(taskHistory.id) }
        }

    @Test
    fun `should call deleteAllHistory from local data source when deleteAll is called from repository`() =
        runBlocking {
            val taskHistory = makeTaskHistory()
            prepareScenario()

            taskHistoryRepository.deleteAll()

            coVerify { taskHistoryLocalDataSource.deleteAllHistory() }
        }

    private fun prepareScenario(
        totalOfNotDoneTasks: Int = 10,
        totalOfDoneTasks: Int = 10,
        taskHistoryId: Long = 45,
        taskHistoryDataList: List<TaskHistoryData> = makeTaskHistoryDataList(),
    ) {
        coEvery { taskHistoryLocalDataSource.getTotalOfNotDoneTasks(any()) } returns totalOfNotDoneTasks
        coEvery { taskHistoryLocalDataSource.getTotalOfDoneTasks(any()) } returns totalOfDoneTasks
        coEvery { taskHistoryLocalDataSource.getTodayHistory(any()) } returns taskHistoryDataList
        coEvery { taskHistoryLocalDataSource.getYesterdayHistory(any()) } returns taskHistoryDataList
        coEvery { taskHistoryLocalDataSource.getPreviousDaysHistory(any()) } returns taskHistoryDataList
        coEvery { taskHistoryLocalDataSource.getTaskHistoryByTask(any()) } returns taskHistoryDataList
        coEvery { taskHistoryLocalDataSource.save(any(), any()) } returns taskHistoryId
    }

}