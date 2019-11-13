package br.com.sailboat.todozy.data.sqlite

import android.content.Context
import android.database.Cursor
import br.com.sailboat.todozy.data.DatabaseOpenHelper
import br.com.sailboat.todozy.domain.filter.BaseFilter
import br.com.sailboat.todozy.data.base.BaseSQLite
import br.com.sailboat.todozy.data.model.TaskData
import br.com.sailboat.todozy.domain.filter.TaskFilter
import br.com.sailboat.todozy.domain.helper.*
import java.util.*

class TaskSQLite(database: DatabaseOpenHelper) : BaseSQLite(database) {

    override val createTableStatement = StringBuilder()
            .append(" CREATE TABLE Task ( ")
            .append(" id INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(" name TEXT NOT NULL, ")
            .append(" notes TEXT, ")
            .append(" insertingDate TEXT, ")
            .append(" enabled INTEGER ")
            .append(" ); ")
            .toString()

    fun getTask(taskId: Long): TaskData? {
        val sb = StringBuilder()
        bindSelect(sb)
        sb.append(" WHERE taskId = $taskId")

        val cursor = performQuery(sb.toString())

        if (cursor.moveToNext()) {
            val task = cursor.mapTask()
            cursor.close()
            return task
        }

        return null
    }

    fun getBeforeTodayTasks(filter: TaskFilter): List<TaskData> {
        val sb = StringBuilder()
        bindSelect(sb)
        bindDefaultWhere(sb)
        bindWhereBeforeToday(sb)
        bindWhereFilter(sb, filter)
        bindOrderBy(sb)

        return getListFromQuery(sb.toString(), filter)
    }

    fun getTodayTasks(filter: TaskFilter): List<TaskData> {
        val sb = StringBuilder()
        bindSelect(sb)
        bindDefaultWhere(sb)
        bindWhereTodayTasks(sb)
        bindWhereFilter(sb, filter)
        bindOrderBy(sb)

        return getListFromQuery(sb.toString(), filter)
    }

    fun getTomorrowTasks(filter: TaskFilter): List<TaskData> {
        val sb = StringBuilder()
        bindSelect(sb)
        bindDefaultWhere(sb)
        bindWhereTomorrowTasks(sb)
        bindWhereFilter(sb, filter)
        bindOrderBy(sb)

        return getListFromQuery(sb.toString(), filter)
    }

    fun getNextDaysTasks(filter: TaskFilter): List<TaskData> {
        val sb = StringBuilder()
        bindSelect(sb)
        bindDefaultWhere(sb)
        bindWhereNextDays(sb)
        bindWhereFilter(sb, filter)
        bindOrderBy(sb)

        return getListFromQuery(sb.toString(), filter)
    }

    fun getTasksThrowBeforeNow(): List<TaskData> {
        val sb = StringBuilder()
        bindSelect(sb)
        sb.append(" WHERE Alarm.nextAlarmDate <= '" + parseCalendarToString(Calendar.getInstance()) + "' ")
        sb.append(" AND Task.enabled = 1 ")
        bindOrderBy(sb)

        return getListFromQuery(sb.toString(), null)
    }

    fun insert(task: TaskData): Long {
        val sql = StringBuilder()

        sql.append(" INSERT INTO Task ")
        sql.append(" (name, notes, insertingDate, enabled) ")
        sql.append(" VALUES (?, ?, ?, ?); ")

        val statement = compileStatement(sql.toString())
        statement.bindString(1, task.name)
        statement.bindString(2, task.notes)
        statement.bindString(3, parseCalendarToString(Calendar.getInstance()))
        statement.bindLong(4, parseBooleanToInt(true).toLong())

        return insert(statement)
    }

    fun update(task: TaskData, enable: Boolean) {
        val sql = StringBuilder()
        sql.append(" UPDATE Task SET ")
        sql.append(" name = ?, ")
        sql.append(" notes = ?, ")
        sql.append(" insertingDate = ?, ")
        sql.append(" enabled = ? ")
        sql.append(" WHERE id = ? ")

        val statement = compileStatement(sql.toString())
        statement.bindString(1, task.name)
        statement.bindString(2, task.notes)
        statement.bindString(3, getCalendarToBind(Calendar.getInstance()))
        statement.bindLong(4, parseBooleanToInt(enable).toLong())
        statement.bindLong(5, task.id)

        update(statement)
    }


    fun getTasksWithAlarms(): List<TaskData> {
        val sb = StringBuilder()
        sb.append(" SELECT Task.id as taskId, Task.name as taskName, ")
        sb.append(" Task.notes as taskNotes ")
        sb.append(" INNER JOIN Alarm ")
        sb.append(" ON (Task.id = Alarm.fkTaskId) ")
        sb.append(" WHERE Task.enabled = 1 ")

        return getListFromQuery(sb.toString(), null)
    }

    private fun getListFromQuery(query: String, filter: BaseFilter?): List<TaskData> {
        val cursor = performQuery(query, filter)
        val tasks = ArrayList<TaskData>()

        while (cursor.moveToNext()) {
            tasks.add(cursor.mapTask())
        }

        cursor.close()

        return tasks
    }

    private fun Cursor.mapTask() = TaskData(
            id = getLong(this, "taskId") ?: EntityHelper.NO_ID,
            name = getString(this, "taskName") ?: "",
            notes = getString(this, "taskNotes"),
            insertingDate = null)


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

        if (filter.text.isNotEmpty()) {
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