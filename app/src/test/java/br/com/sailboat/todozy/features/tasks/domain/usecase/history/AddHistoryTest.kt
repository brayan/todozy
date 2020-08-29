package br.com.sailboat.todozy.features.tasks.domain.usecase.history

import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.features.tasks.domain.model.Alarm
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.features.tasks.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository
import br.com.sailboat.todozy.features.tasks.domain.usecase.tasks.CheckTaskFields
import br.com.sailboat.todozy.features.tasks.domain.usecase.tasks.SaveTask
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*

class AddHistoryTest {

    private val repository: TaskHistoryRepository = mockk(relaxed = true)

    private lateinit var addHistory: AddHistory

    @Before
    fun setUp() {
        addHistory = AddHistory(repository)
    }

    @Test
    fun `should insert task in the repository`() = runBlocking {
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