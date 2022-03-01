package br.com.sailboat.todozy.features.tasks.data.datasource.local

import androidx.test.platform.app.InstrumentationRegistry
import br.com.sailboat.todozy.core.platform.DatabaseOpenHelper
import br.com.sailboat.todozy.feature.task.list.data.datasource.TaskLocalDataSourceSQLite
import br.com.sailboat.todozy.utility.kotlin.exception.EntityNotFoundException
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class TaskLocalDataSourceSQLiteTest {

    private lateinit var taskSQLite: TaskLocalDataSourceSQLite

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        taskSQLite = TaskLocalDataSourceSQLite(DatabaseOpenHelper(context))
    }

    @After
    fun tearDown() {
        val context = InstrumentationRegistry.getInstrumentation().context
        context.deleteDatabase(DatabaseOpenHelper.DATABASE_NAME)
    }

    @Test
    @Throws(EntityNotFoundException::class)
    fun shouldThrowEntityNotFoundExceptionWhenSearchingByTask() {
        runBlocking {
            taskSQLite.getTask(12)
        }
    }
}