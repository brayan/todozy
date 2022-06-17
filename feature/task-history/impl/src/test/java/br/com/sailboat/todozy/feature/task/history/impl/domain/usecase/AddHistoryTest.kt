package br.com.sailboat.todozy.feature.task.history.impl.domain.usecase

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.history.domain.repository.TaskHistoryRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.Calendar

class AddHistoryTest {

    private val repository: TaskHistoryRepository = mockk(relaxed = true)

    private val addHistory = AddHistory(repository)

    @Test
    fun `should insert history in the repository`() = runBlocking {
        val dateTime = Calendar.getInstance()
        val alarm = Alarm(
            dateTime = dateTime,
            repeatType = RepeatType.DAY
        )
        val task = Task(id = 45, name = "Task Name", notes = "Some notes", alarm = alarm)

        addHistory(task, TaskStatus.DONE)

        coVerify { repository.insert(task, TaskStatus.DONE) }
        confirmVerified(repository)
    }
}
