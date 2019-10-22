package br.com.sailboat.todozy.data.repository.mock

import android.content.Context
import br.com.sailboat.todozy.data.mapper.mapToAlarm
import br.com.sailboat.todozy.data.mapper.mapToTask
import br.com.sailboat.todozy.data.mapper.mapToTaskData
import br.com.sailboat.todozy.data.model.TaskData
import br.com.sailboat.todozy.data.sqlite.AlarmSQLite
import br.com.sailboat.todozy.data.sqlite.TaskSQLite
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.repository.TaskRepository
import br.com.sailboat.todozy.domain.filter.TaskFilter
import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import java.util.*

class TaskRepositoryMock : TaskRepository {

//    private val taskDao by lazy { TaskSQLite(context) }
//    private val alarmDao by lazy { AlarmSQLite(context) }

    override suspend fun getTask(taskId: Long): Task? {
        val alarm = Alarm(dateTime = Calendar.getInstance(),
                repeatType = RepeatType.DAY)

        return Task(id = 12,
                name = "Plan the day \uD83D\uDCD3",
                notes = "Write in details what I will do today.",
                alarm = alarm)
    }

    override suspend fun getBeforeTodayTasks(filter: TaskFilter): List<Task> {

        val tasks = mutableListOf<Task>()



        for (i in 1..10) {
            val dateTime = Calendar.getInstance()
            dateTime.add(Calendar.DAY_OF_MONTH, -i)

            val alarm = Alarm(dateTime = dateTime,
                    repeatType = RepeatType.DAY)

            tasks.add(Task(id = 1,
                    name = "Task $i",
                    notes = "Task $i",
                    alarm = alarm))
        }

        return tasks

        // Drink 2L of water
        // Programming workout
        // Design workout
        // Body workout
        // Pay bills
        // Coffee break
        // Check my emails
        // Read a book
        // Download weekly podcasts
    }

    override suspend fun getTodayTasks(filter: TaskFilter): List<Task> {
        return mutableListOf<Task>()
    }

    override suspend fun getTomorrowTasks(filter: TaskFilter): List<Task> {
        return mutableListOf<Task>()
    }

    override suspend fun getNextDaysTasks(filter: TaskFilter): List<Task> {
        return mutableListOf<Task>()
    }

    override suspend fun getTasksThrowBeforeNow(): List<Task> {
        return mutableListOf<Task>()
    }

    override suspend fun getTasksWithAlarms(): List<Task> {
        return mutableListOf<Task>()
    }

    override suspend fun insert(task: Task) {

    }

    override suspend fun update(task: Task) {

    }

    override suspend fun disableTask(task: Task) {

    }


}