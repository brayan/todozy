package br.com.sailboat.todozy.features.tasks.data.repository

import br.com.sailboat.todozy.features.tasks.data.datasource.local.TaskHistoryLocalDataSource
import br.com.sailboat.todozy.features.tasks.data.factory.TaskDataMockFactory
import br.com.sailboat.todozy.features.tasks.data.factory.TaskHistoryDataMockFactory
import br.com.sailboat.todozy.features.tasks.data.factory.TaskHistoryDataMockFactory.makeTaskHistoryData
import br.com.sailboat.todozy.features.tasks.data.factory.TaskHistoryDataMockFactory.makeTaskHistoryDataList
import br.com.sailboat.todozy.features.tasks.data.model.TaskData
import br.com.sailboat.todozy.features.tasks.data.model.TaskHistoryData
import br.com.sailboat.todozy.features.tasks.domain.factory.AlarmMockFactory
import br.com.sailboat.todozy.features.tasks.domain.factory.TaskHistoryMockFactory.makeTaskHistory
import br.com.sailboat.todozy.features.tasks.domain.factory.TaskHistoryMockFactory.makeTaskHistoryList
import br.com.sailboat.todozy.features.tasks.domain.model.*
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals

class TaskHistoryRepositoryImplTest {

    private val taskHistoryLocalDataSource: TaskHistoryLocalDataSource = mockk(relaxed = true)

    private val taskHistoryRepository = TaskHistoryRepositoryImpl(
        taskHistoryLocalDataSource = taskHistoryLocalDataSource,
    )

//    fun getTodayHistory(filter: TaskHistoryFilter): List<TaskHistoryData>
//    fun getYesterdayHistory(filter: TaskHistoryFilter): List<TaskHistoryData>
//    fun getPreviousDaysHistory(filter: TaskHistoryFilter): List<TaskHistoryData>
//    fun save(taskId: Long, taskStatus: Int): Long
//    fun update(taskHistoryData: TaskHistoryData)
//    fun delete(taskHistoryId: Long)
//    fun deleteAllHistory()
//    fun getTotalOfNotDoneTasks(filter: TaskHistoryFilter): Int
//    fun getTotalOfDoneTasks(filter: TaskHistoryFilter): Int
//    fun getTaskHistoryByTask(taskId: Long): List<TaskHistoryData>

//    @Test
//    fun `should call getBeforeTodayTasks from taskLocalDataSource when getBeforeTodayTasks is called from taskRepository`() =
//        runBlocking {
//            val taskDataResult = TaskDataMockFactory.makeTaskData()
//            val taskHistoryFilter = TaskHistoryFilter()
//            prepareScenario()
//
//            val result = taskHistoryRepository.getTodayHistory(taskHistoryFilter)
//
//            val expected = listOf(
//                TaskHistory(
//                    id = taskDataResult.id,
//                    name = taskDataResult.name.orEmpty(),
//                    notes = taskDataResult.notes,
//                    alarm = alarm
//                )
//            )
//            assertEquals(expected, result)
//            coVerify { taskHistoryLocalDataSource.getTodayHistory(taskHistoryFilter) }
//        }

    private fun prepareScenario(
        taskHistory: TaskHistory = makeTaskHistory(),
        taskHistoryList: List<TaskHistory> = makeTaskHistoryList(),
    ) {
        coEvery { taskHistoryRepository.getTodayHistory(any()) } returns taskHistoryList
    }


}