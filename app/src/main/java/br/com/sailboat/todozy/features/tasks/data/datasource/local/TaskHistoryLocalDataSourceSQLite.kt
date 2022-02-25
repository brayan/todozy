package br.com.sailboat.todozy.features.tasks.data.datasource.local

import android.database.Cursor
import br.com.sailboat.todozy.utility.kotlin.model.BaseFilter
import br.com.sailboat.todozy.utility.android.sqlite.BaseSQLite
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import br.com.sailboat.todozy.utility.kotlin.exception.EntityNotFoundException
import br.com.sailboat.todozy.core.platform.DatabaseOpenHelper
import br.com.sailboat.todozy.core.platform.TaskHistoryQueryBuilder
import br.com.sailboat.todozy.features.tasks.data.model.TaskHistoryData
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import java.util.*

class TaskHistoryLocalDataSourceSQLite(database: DatabaseOpenHelper) :
    BaseSQLite(database),
    TaskHistoryLocalDataSource {

    override val createTableStatement = StringBuilder()
        .append(" CREATE TABLE TaskHistory ( ")
        .append(" id INTEGER PRIMARY KEY AUTOINCREMENT, ")
        .append(" fkTaskId INTEGER, ")
        .append(" status INTEGER, ")
        .append(" insertingDate TEXT, ")
        .append(" enabled INTEGER, ")
        .append(" FOREIGN KEY(fkTaskId) REFERENCES Task(id) ")
        .append(" ); ")
        .toString()


    override fun save(taskId: Long, taskStatus: Int): Long {
        val sql = StringBuilder()

        sql.append(" INSERT INTO TaskHistory ")
        sql.append(" (fkTaskId, status, insertingDate, enabled) ")
        sql.append(" VALUES (?, ?, ?, ?); ")

        val statement = compileStatement(sql.toString())
        statement.bindLong(1, taskId)
        statement.bindLong(2, taskStatus.toLong())
        statement.bindString(3, parseCalendarToString(Calendar.getInstance()))
        statement.bindLong(4, parseBooleanToInt(true).toLong())

        return insert(statement)
    }

    override fun update(taskHistoryData: TaskHistoryData) {
        val sql = StringBuilder()
        sql.append(" UPDATE TaskHistory SET ")
        sql.append(" status = ?, ")
        sql.append(" enabled = ? ")
        sql.append(" WHERE id = ? ")

        val statement = compileStatement(sql.toString())
        statement.bindLong(1, taskHistoryData.status.toLong())
        statement.bindLong(2, parseBooleanToInt(taskHistoryData.enabled).toLong())
        statement.bindLong(3, taskHistoryData.id)

        update(statement)
    }

    override fun delete(taskHistoryId: Long) {
        val sql = StringBuilder()
        sql.append(" DELETE FROM TaskHistory WHERE TaskHistory.id = ?")

        val statement = compileStatement(sql.toString())
        statement.bindLong(1, taskHistoryId)

        delete(statement)
    }

    override fun deleteAllHistory() {
        val delete = " DELETE FROM TaskHistory"
        delete(compileStatement(delete))
    }

    override fun getTodayHistory(filter: TaskHistoryFilter): List<TaskHistoryData> {
        val query = TaskHistoryQueryBuilder()
        query.bindDefaultSelect()
        query.bindDefaultInnerJoin()
        query.bindDefaultWhere()
        query.bindWhereToday()
        query.bindWhereEnabled()
        query.bindWhereFilter(filter)
        query.bindWhereDateRange(filter)
        query.bindDefaultOrderBy()

        return getTaskHistoryList(query.toString(), filter)
    }

    override fun getYesterdayHistory(filter: TaskHistoryFilter): List<TaskHistoryData> {
        val query = TaskHistoryQueryBuilder()
        query.bindDefaultSelect()
        query.bindDefaultInnerJoin()
        query.bindDefaultWhere()
        query.bindWhereYesterday()
        query.bindWhereEnabled()
        query.bindWhereFilter(filter)
        query.bindWhereDateRange(filter)
        query.bindDefaultOrderBy()

        return getTaskHistoryList(query.toString(), filter)
    }

    override fun getPreviousDaysHistory(filter: TaskHistoryFilter): List<TaskHistoryData> {
        val query = TaskHistoryQueryBuilder()
        query.bindDefaultSelect()
        query.bindDefaultInnerJoin()
        query.bindDefaultWhere()
        query.bindWherePreviousDays()
        query.bindWhereEnabled()
        query.bindWhereFilter(filter)
        query.bindWhereDateRange(filter)
        query.bindDefaultOrderBy()

        return getTaskHistoryList(query.toString(), filter)
    }

    override fun getTaskHistoryByTask(taskId: Long): List<TaskHistoryData> {
        val query = TaskHistoryQueryBuilder()
        query.bindDefaultSelect()
        query.bindDefaultInnerJoin()
        query.bindDefaultWhere()
        query.bindWhereTaskId(taskId)
        query.bindDefaultOrderBy()

        return getTaskHistoryList(query.toString(), null)
    }

    fun getPreviousDaysHistoryFromDate(
        initialDate: Calendar,
        finalDate: Calendar,
        filter: TaskHistoryFilter
    ): List<TaskHistoryData> {
        val query = TaskHistoryQueryBuilder()
        query.bindDefaultSelect()
        query.bindDefaultInnerJoin()
        query.bindDefaultWhere()
        query.bindWherePreviousDays(initialDate, finalDate)
        query.bindWhereEnabled()
        query.bindWhereFilter(filter)
        query.bindDefaultOrderBy()

        return getTaskHistoryList(query.toString(), filter)
    }

    override fun getTotalOfDoneTasks(filter: TaskHistoryFilter): Int {
        val query = TaskHistoryQueryBuilder()
        query.bindSelectCount()
        query.bindDefaultInnerJoin()
        query.bindDefaultWhere()
        query.bindWhereDateRange(filter)
        query.bindWhereTaskDone()
        query.bindWhereFilter(filter)

        return getCountFromQuery(query.toString(), filter)
    }

    override fun getTotalOfNotDoneTasks(filter: TaskHistoryFilter): Int {
        val query = TaskHistoryQueryBuilder()
        query.bindSelectCount()
        query.bindDefaultInnerJoin()
        query.bindDefaultWhere()
        query.bindWhereDateRange(filter)
        query.bindWhereTaskNotDone()
        query.bindWhereFilter(filter)

        return getCountFromQuery(query.toString(), filter)
    }

    fun getTotalOfDoneTasks(taskId: Long): Int {
        val query = TaskHistoryQueryBuilder()
        query.bindSelectCount()
        query.bindDefaultInnerJoin()
        query.bindDefaultWhere()
        query.bindWhereTaskId(taskId)
        query.bindWhereTaskDone()

        return getCountFromQuery(query.toString())
    }

    fun getTotalOfNotDoneTasks(taskId: Long): Int {
        val query = TaskHistoryQueryBuilder()
        query.bindSelectCount()
        query.bindDefaultInnerJoin()
        query.bindDefaultWhere()
        query.bindWhereTaskId(taskId)
        query.bindWhereTaskNotDone()

        return getCountFromQuery(query.toString())
    }

    fun getTaskHistoryById(taskHistoryId: Long): TaskHistoryData {
        val sb = StringBuilder()
        sb.append(" SELECT TaskHistory.*, Task.name FROM TaskHistory ")
        sb.append(" INNER JOIN Task ON (TaskHistory.fkTaskId = Task.id) ")
        sb.append(" WHERE TaskHistory.id = $taskHistoryId")

        val cursor = performQuery(sb.toString())

        if (cursor.moveToNext()) {
            val taskHistory = cursor.mapToTaskHistoryData()
            cursor.close()
            return taskHistory
        }

        throw EntityNotFoundException()
    }

    private fun getTaskHistoryList(sql: String, filter: BaseFilter?): List<TaskHistoryData> {
        val cursor = performQuery(sql, filter)

        val historyList = ArrayList<TaskHistoryData>()

        if (cursor.moveToNext()) {
            do {
                val taskHistory = cursor.mapToTaskHistoryData()
                historyList.add(taskHistory)
                cursor.moveToNext()
            } while (!cursor.isAfterLast)

        }
        cursor.close()
        return historyList
    }

    private fun Cursor.mapToTaskHistoryData() =
        TaskHistoryData(
            id = getLong(this, "id") ?: Entity.NO_ID,
            taskId = getLong(this, "fkTaskId") ?: Entity.NO_ID,
            taskName = getString(this, "name"),
            status = getInt(this, "status") ?: TaskStatus.DONE.id,
            insertingDate = getString(this, "insertingDate"),
            enabled = getBoolean(this, "enabled") ?: true
        )

}