package br.com.sailboat.todozy.feature.task.history.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistory
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskProgressFilter
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import kotlin.test.assertEquals

internal class GetTaskProgressUseCaseImplTest {
    private val taskHistoryRepository: TaskHistoryRepository = mockk(relaxed = true)
    private val clock: Clock = Clock.fixed(Instant.parse("2024-08-15T12:00:00Z"), ZoneId.of("UTC"))

    private val getTaskProgressUseCase = GetTaskProgressUseCaseImpl(taskHistoryRepository, clock)

    @Test
    fun `should map progress grouped by day within selected range`() =
        runTest {
            val filter = TaskProgressFilter(range = TaskProgressRange.LAST_7_DAYS, taskId = 10L)
            val history =
                listOf(
                    TaskHistory(
                        id = 1,
                        taskId = 10L,
                        taskName = "Task",
                        status = TaskStatus.DONE,
                        insertingDate = "2024-08-14 08:00:00",
                    ),
                    TaskHistory(
                        id = 2,
                        taskId = 10L,
                        taskName = "Task",
                        status = TaskStatus.NOT_DONE,
                        insertingDate = "2024-08-13 08:00:00",
                    ),
                    TaskHistory(
                        id = 3,
                        taskId = 10L,
                        taskName = "Task",
                        status = TaskStatus.DONE,
                        insertingDate = "2024-08-13 10:00:00",
                    ),
                )

            coEvery { taskHistoryRepository.getHistory(any()) } returns Result.success(history)

            val progressDays = getTaskProgressUseCase(filter).getOrThrow()

            assertEquals(7, progressDays.size)

            val august14 = progressDays.first { it.date == LocalDate.of(2024, 8, 14) }
            assertEquals(1, august14.doneCount)
            assertEquals(1, august14.totalCount)

            val august13 = progressDays.first { it.date == LocalDate.of(2024, 8, 13) }
            assertEquals(1, august13.doneCount)
            assertEquals(2, august13.totalCount)

            val august12 = progressDays.first { it.date == LocalDate.of(2024, 8, 12) }
            assertEquals(0, august12.doneCount)
            assertEquals(0, august12.totalCount)

            coVerify {
                taskHistoryRepository.getHistory(
                    withArg {
                        assertEquals(10L, it.taskId)
                        assertEquals(LocalDate.of(2024, 8, 9), it.initialDate?.toInstant()?.atZone(ZoneId.of("UTC"))?.toLocalDate())
                        assertEquals(LocalDate.of(2024, 8, 15), it.finalDate?.toInstant()?.atZone(ZoneId.of("UTC"))?.toLocalDate())
                    },
                )
            }
        }
}
