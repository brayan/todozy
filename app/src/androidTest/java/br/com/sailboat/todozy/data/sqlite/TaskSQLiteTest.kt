package br.com.sailboat.todozy.data.sqlite

import androidx.test.platform.app.InstrumentationRegistry
import br.com.sailboat.todozy.data.DatabaseOpenHelper
import br.com.sailboat.todozy.domain.exceptions.EntityNotFoundException
import org.junit.After
import org.junit.Before
import org.junit.Test


class TaskSQLiteTest {

    private lateinit var taskSQLite: TaskSQLite

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        taskSQLite = TaskSQLite(context)
    }

    @After
    fun tearDown() {
        val context = InstrumentationRegistry.getInstrumentation().context
        context.deleteDatabase(DatabaseOpenHelper.DATABASE_NAME)
    }

    @Test
    @Throws(EntityNotFoundException::class)
    fun shouldThrowEntityNotFoundExceptionWhenSearchingByTask() {
        taskSQLite.getTask(12)
    }

}