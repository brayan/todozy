package br.com.sailboat.todozy.feature.task.history.impl.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.history.impl.domain.service.CalendarService
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.DeleteAllHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.DeleteHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.GetTaskHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.domain.usecase.UpdateHistoryUseCase
import br.com.sailboat.todozy.feature.task.history.impl.presentation.GetDateFilterNameViewUseCase
import br.com.sailboat.todozy.feature.task.history.impl.presentation.factory.TaskHistoryUiModelFactory
import br.com.sailboat.todozy.feature.task.history.impl.presentation.mapper.TaskHistoryUiModelToTaskHistoryMapper
import br.com.sailboat.todozy.utility.kotlin.LogService
import br.com.sailboat.todozy.utility.kotlin.extension.toEndOfDayCalendar
import br.com.sailboat.todozy.utility.kotlin.extension.toStartOfDayCalendar
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.DateFilterTaskHistorySelectableItem
import br.com.sailboat.uicomponent.impl.helper.CoroutinesTestRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
internal class TaskHistoryViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val getTaskMetricsUseCase: GetTaskMetricsUseCase = mockk()
    private val getTaskHistoryUseCase: GetTaskHistoryUseCase = mockk()
    private val getDateFilterNameViewUseCase: GetDateFilterNameViewUseCase = mockk(relaxed = true)
    private val updateHistoryUseCase: UpdateHistoryUseCase = mockk(relaxed = true)
    private val deleteHistoryUseCase: DeleteHistoryUseCase = mockk(relaxed = true)
    private val deleteAllHistoryUseCase: DeleteAllHistoryUseCase = mockk(relaxed = true)
    private val taskHistoryUiModelFactory: TaskHistoryUiModelFactory = mockk()
    private val taskHistoryUiModelToTaskHistoryMapper: TaskHistoryUiModelToTaskHistoryMapper = mockk(relaxed = true)
    private val logService: LogService = mockk(relaxed = true)
    private val calendarService: CalendarService = mockk(relaxed = true)

    private lateinit var viewModel: TaskHistoryViewModel

    @Before
    fun setup() {
        viewModel =
            TaskHistoryViewModel(
                getTaskMetricsUseCase = getTaskMetricsUseCase,
                getTaskHistoryUseCase = getTaskHistoryUseCase,
                getDateFilterNameViewUseCase = getDateFilterNameViewUseCase,
                updateHistoryUseCase = updateHistoryUseCase,
                deleteHistoryUseCase = deleteHistoryUseCase,
                deleteAllHistoryUseCase = deleteAllHistoryUseCase,
                taskHistoryUiModelFactory = taskHistoryUiModelFactory,
                taskHistoryUiModelToTaskHistoryMapper = taskHistoryUiModelToTaskHistoryMapper,
                logService = logService,
                calendarService = calendarService,
            )
    }

    @Test
    fun `should apply a 7-day inclusive range when last seven days filter is selected`() = runTest(coroutinesTestRule.dispatcher) {
        val metricsFilter = slot<TaskHistoryFilter>()
        coEvery { getTaskMetricsUseCase(capture(metricsFilter)) } returns Result.success(TaskMetrics(0, 0, 0))
        coEvery { getTaskHistoryUseCase(any()) } answers { Result.success(emptyList()) }
        every { taskHistoryUiModelFactory.create(any(), any()) } returns emptyList()

        viewModel.dispatchViewIntent(
            TaskHistoryViewIntent.OnSelectDateFromFilter(DateFilterTaskHistorySelectableItem.LAST_7_DAYS),
        )
        advanceUntilIdle()

        val today = LocalDate.now()
        val expectedInitialDate = TaskProgressRange.LAST_7_DAYS.startDate(today).toStartOfDayCalendar()
        val expectedFinalDate = today.toEndOfDayCalendar()
        val capturedFilter = metricsFilter.captured

        assertEquals(expectedInitialDate.timeInMillis, capturedFilter.initialDate?.timeInMillis)
        assertEquals(expectedFinalDate.timeInMillis, capturedFilter.finalDate?.timeInMillis)
    }

    @Test
    fun `should apply a 30-day inclusive range when last thirty days filter is selected`() = runTest(coroutinesTestRule.dispatcher) {
        val metricsFilter = slot<TaskHistoryFilter>()
        coEvery { getTaskMetricsUseCase(capture(metricsFilter)) } returns Result.success(TaskMetrics(0, 0, 0))
        coEvery { getTaskHistoryUseCase(any()) } answers { Result.success(emptyList()) }
        every { taskHistoryUiModelFactory.create(any(), any()) } returns emptyList()

        viewModel.dispatchViewIntent(
            TaskHistoryViewIntent.OnSelectDateFromFilter(DateFilterTaskHistorySelectableItem.LAST_30_DAYS),
        )
        advanceUntilIdle()

        val today = LocalDate.now()
        val expectedInitialDate = TaskProgressRange.LAST_30_DAYS.startDate(today).toStartOfDayCalendar()
        val expectedFinalDate = today.toEndOfDayCalendar()
        val capturedFilter = metricsFilter.captured

        assertEquals(expectedInitialDate.timeInMillis, capturedFilter.initialDate?.timeInMillis)
        assertEquals(expectedFinalDate.timeInMillis, capturedFilter.finalDate?.timeInMillis)
    }
}
