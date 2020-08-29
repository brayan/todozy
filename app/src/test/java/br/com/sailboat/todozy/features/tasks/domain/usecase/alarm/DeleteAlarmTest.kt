package br.com.sailboat.todozy.features.tasks.domain.usecase.alarm

import br.com.sailboat.todozy.features.tasks.domain.model.*
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.features.tasks.domain.usecase.history.DeleteHistory
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class DeleteAlarmTest {

    private val repository: AlarmRepository = mockk(relaxed = true)

    private lateinit var deleteAlarm: DeleteAlarm

    @Before
    fun setUp() {
        deleteAlarm = DeleteAlarm(repository)
    }

    @Test
    fun `should delete alarm from repository`() = runBlocking {
        val task = Task(id = 45, name = "Task Name", notes = "Some notes", alarm = Alarm(
                dateTime = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, -1) },
                repeatType = RepeatType.NOT_REPEAT
        ))

        deleteAlarm(task)

        coVerify { repository.deleteAlarmByTask(task) }
        confirmVerified(repository)
    }

}