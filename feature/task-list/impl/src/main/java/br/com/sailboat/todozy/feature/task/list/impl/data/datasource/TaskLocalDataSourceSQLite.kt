package br.com.sailboat.todozy.feature.task.list.impl.data.datasource

import android.database.Cursor
import br.com.sailboat.todozy.domain.model.TaskFilter
import br.com.sailboat.todozy.feature.task.list.impl.data.model.TaskData
import br.com.sailboat.todozy.utility.android.sqlite.BaseSQLite
import br.com.sailboat.todozy.utility.android.sqlite.DatabaseOpenHelperService
import br.com.sailboat.todozy.utility.kotlin.exception.EntityNotFoundException
import br.com.sailboat.todozy.utility.kotlin.extension.getFinalCalendarForToday
import br.com.sailboat.todozy.utility.kotlin.extension.getFinalCalendarForTomorrow
import br.com.sailboat.todozy.utility.kotlin.extension.getInitialCalendarForToday
import br.com.sailboat.todozy.utility.kotlin.extension.getInitialCalendarForTomorrow
import br.com.sailboat.todozy.utility.kotlin.extension.isTrue
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import br.com.sailboat.todozy.utility.kotlin.model.Filter
import java.util.Calendar

internal class TaskLocalDataSourceSQLite(
    database: DatabaseOpenHelperService,
) : BaseSQLite(database), TaskLocalDataSource {

    override suspend fun getTask(taskId: Long): Result<TaskData> = runCatching {
        val sb = StringBuilder()
        bindSelect(sb)
        sb.append(" WHERE taskId = $taskId")

        val cursor = performQuery(sb.toString())

        if (cursor.moveToNext()) {
            val task = cursor.mapTask()
            cursor.close()
            return@runCatching task
        }

        throw EntityNotFoundException()
    }

    override suspend fun getBeforeTodayTasks(filter: TaskFilter) = runCatching {
        val sb = StringBuilder()
        bindSelect(sb)
        bindDefaultWhere(sb)
        bindWhereBeforeToday(sb)
        bindWhereFilter(sb, filter)
        bindOrderBy(sb)

        return@runCatching getListFromQuery(sb.toString(), filter)
    }

    override suspend fun getTodayTasks(filter: TaskFilter): Result<List<TaskData>> = runCatching {
        val sb = StringBuilder()
        bindSelect(sb)
        bindDefaultWhere(sb)
        bindWhereTodayTasks(sb)
        bindWhereFilter(sb, filter)
        bindOrderBy(sb)

        return@runCatching getListFromQuery(sb.toString(), filter)
    }

    override suspend fun getTomorrowTasks(filter: TaskFilter) = runCatching {
        val sb = StringBuilder()
        bindSelect(sb)
        bindDefaultWhere(sb)
        bindWhereTomorrowTasks(sb)
        bindWhereFilter(sb, filter)
        bindOrderBy(sb)

        return@runCatching getListFromQuery(sb.toString(), filter)
    }

    override suspend fun getNextDaysTasks(filter: TaskFilter) = runCatching {
        val sb = StringBuilder()
        bindSelect(sb)
        bindDefaultWhere(sb)
        bindWhereNextDays(sb)
        bindWhereFilter(sb, filter)
        bindOrderBy(sb)

        return@runCatching getListFromQuery(sb.toString(), filter)
    }

    override suspend fun getTasksThrowBeforeNow(): Result<List<TaskData>> = runCatching {
        val sb = StringBuilder()
        bindSelect(sb)
        sb.append(" WHERE (Alarm.nextAlarmDate <= '" + parseCalendarToString(Calendar.getInstance()) + "' ")
        sb.append(" OR Alarm.nextAlarmDate is null) ")
        sb.append(" AND Task.enabled = 1 ")
        bindOrderBy(sb)

        return@runCatching getListFromQuery(sb.toString(), null)
    }

    override suspend fun insert(taskData: TaskData): Result<Long> = runCatching {
        val sql = StringBuilder()

        sql.append(" INSERT INTO Task ")
        sql.append(" (name, notes, insertingDate, enabled) ")
        sql.append(" VALUES (?, ?, ?, ?); ")

        val statement = compileStatement(sql.toString())
        statement.bindString(1, taskData.name)
        statement.bindString(2, taskData.notes)
        statement.bindString(3, parseCalendarToString(Calendar.getInstance()))
        statement.bindLong(4, parseBooleanToInt(true).toLong())

        return@runCatching insert(statement)
    }

    override suspend fun update(taskData: TaskData, enabled: Boolean): Result<Unit?> = runCatching {
        val sql = StringBuilder()
        sql.append(" UPDATE Task SET ")
        sql.append(" name = ?, ")
        sql.append(" notes = ?, ")
        sql.append(" insertingDate = ?, ")
        sql.append(" enabled = ? ")
        sql.append(" WHERE id = ? ")

        val statement = compileStatement(sql.toString())
        statement.bindString(1, taskData.name)
        statement.bindString(2, taskData.notes)
        statement.bindString(3, getCalendarToBind(Calendar.getInstance()))
        statement.bindLong(4, parseBooleanToInt(enabled).toLong())
        statement.bindLong(5, taskData.id)

        update(statement)
    }

    override suspend fun getTasksWithAlarms(): Result<List<TaskData>> = runCatching {
        val sb = StringBuilder()
        sb.append(" SELECT Task.id as taskId, Task.name as taskName, ")
        sb.append(" Task.notes as taskNotes ")
        sb.append(" FROM Task INNER JOIN Alarm ")
        sb.append(" ON (Task.id = Alarm.fkTaskId) ")
        sb.append(" WHERE Task.enabled = 1 ")

        return@runCatching getListFromQuery(sb.toString(), null)
    }

    private fun getListFromQuery(query: String, filter: Filter?): List<TaskData> {
        val cursor = performQuery(query, filter)
        val tasks = ArrayList<TaskData>()

        while (cursor.moveToNext()) {
            tasks.add(cursor.mapTask())
        }

        cursor.close()

        return tasks
    }

    private fun Cursor.mapTask() = TaskData(
        id = getLong(this, "taskId") ?: Entity.NO_ID,
        name = getString(this, "taskName") ?: "",
        notes = getString(this, "taskNotes"),
        insertingDate = null
    )

    private fun bindSelect(sb: StringBuilder) {
        sb.append(" SELECT Task.id as taskId, Task.name as taskName, ")
        sb.append(" Task.notes as taskNotes ")
        sb.append(" FROM Task LEFT JOIN Alarm ")
        sb.append(" ON (Task.id = Alarm.fkTaskId) ")
    }

    private fun bindDefaultWhere(sb: StringBuilder) {
        sb.append(" WHERE 1=1 ")
    }

    private fun bindWhereTodayTasks(sb: StringBuilder) {
        sb.append(" AND ")
        sb.append(" ((Alarm.nextAlarmDate >= ")
        sb.append(" '" + parseCalendarToString(getInitialCalendarForToday()) + "' ")
        sb.append(" AND Alarm.nextAlarmDate <= ")
        sb.append(" '" + parseCalendarToString(getFinalCalendarForToday()) + "') ")
        sb.append(" OR Alarm.nextAlarmDate is null) ")
        sb.append(" AND Task.enabled = 1 ")
    }

    private fun bindWhereBeforeToday(sb: StringBuilder) {
        sb.append(" AND (Alarm.nextAlarmDate <= ")
        sb.append(" '" + parseCalendarToString(getInitialCalendarForToday()) + "') ")
        sb.append(" AND Task.enabled = 1 ")
    }

    private fun bindWhereTomorrowTasks(sb: StringBuilder) {
        sb.append(" AND (Alarm.nextAlarmDate >= ")
        sb.append(" '" + parseCalendarToString(getInitialCalendarForTomorrow()) + "' ")
        sb.append(" AND Alarm.nextAlarmDate <= ")
        sb.append(" '" + parseCalendarToString(getFinalCalendarForTomorrow()) + "') ")
        sb.append(" AND Task.enabled = 1 ")
    }

    private fun bindWhereNextDays(sb: StringBuilder) {
        sb.append(" AND Alarm.nextAlarmDate > ")
        sb.append(" '" + parseCalendarToString(getFinalCalendarForTomorrow()) + "' ")
        sb.append(" AND Task.enabled = 1 ")
    }

    private fun bindWhereFilter(sb: StringBuilder, filter: TaskFilter?) {
        if (filter == null) {
            return
        }

        if (filter.text?.isNotEmpty().isTrue()) {
            sb.append(" AND Task.name LIKE ? ")
        }
    }

    private fun bindOrderBy(sb: StringBuilder) {
        sb.append(" ORDER BY Alarm.nextAlarmDate, Task.insertingDate DESC ")
    }

    private fun bindWhereTask(sb: StringBuilder, taskId: Long) {
        sb.append(" AND Task.id = $taskId")
    }
}
