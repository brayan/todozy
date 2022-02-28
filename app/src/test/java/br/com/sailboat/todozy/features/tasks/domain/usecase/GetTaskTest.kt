package br.com.sailboat.todozy.features.tasks.domain.usecase

import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class GetTaskTest {

    private val repository: TaskRepository = mockk(relaxed = true)

    private lateinit var getTask: GetTask

    @Before
    fun setUp() {
        getTask = GetTask(repository)
    }

    @Test
    fun `should get task from repository`() = runBlocking {
        val task = Task(id = 45, name = "Task Name", notes = "Some notes")
        coEvery { repository.getTask(any()) } returns task

        val result = getTask(45)

        coVerify { repository.getTask(45) }
        confirmVerified(repository)
        assertEquals(result, task)
    }

}