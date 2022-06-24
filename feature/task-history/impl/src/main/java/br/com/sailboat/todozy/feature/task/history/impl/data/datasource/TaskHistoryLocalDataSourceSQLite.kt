package br.com.sailboat.todozy.feature.task.history.impl.data.datasource

import android.database.Cursor
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.history.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.history.impl.data.model.TaskHistoryData
import br.com.sailboat.todozy.utility.android.sqlite.BaseSQLite
import br.com.sailboat.todozy.utility.android.sqlite.DatabaseOpenHelperService
import br.com.sailboat.todozy.utility.kotlin.exception.EntityNotFoundException
import br.com.sailboat.todozy.utility.kotlin.model.BaseFilter
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import java.util.Calendar

internal class TaskHistoryLocalDataSourceSQLite(
    database: DatabaseOpenHelperService,
) : BaseSQLite(database), TaskHistoryLocalDataSource {

    override fun save(taskId: Long, taskStatus: Int): Result<Long> = runCatching {
        val sql = StringBuilder()

        sql.append(" INSERT INTO TaskHistory ")
        sql.append(" (fkTaskId, status, insertingDate, enabled) ")
        sql.append(" VALUES (?, ?, ?, ?); ")

        val statement = compileStatement(sql.toString())
        statement.bindLong(1, taskId)
        statement.bindLong(2, taskStatus.toLong())
        statement.bindString(3, parseCalendarToString(Calendar.getInstance()))
        statement.bindLong(4, parseBooleanToInt(true).toLong())

        return@runCatching insert(statement)
    }

    override fun update(taskHistoryData: TaskHistoryData) = runCatching {
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

    override fun delete(taskHistoryId: Long) = runCatching {
        val sql = StringBuilder()
        sql.append(" DELETE FROM TaskHistory WHERE TaskHistory.id = ?")

        val statement = compileStatement(sql.toString())
        statement.bindLong(1, taskHistoryId)

        delete(statement)
    }

    override fun deleteAllHistory() = runCatching {
        val delete = " DELETE FROM TaskHistory"
        delete(compileStatement(delete))
    }

    override fun getTodayHistory(filter: TaskHistoryFilter) = runCatching {
        val query = TaskHistoryQueryBuilder()
        query.bindDefaultSelect()
        query.bindDefaultInnerJoin()
        query.bindDefaultWhere()
        query.bindWhereToday()
        query.bindWhereEnabled()
        query.bindWhereFilter(filter)
        query.bindWhereDateRange(filter)
        query.bindDefaultOrderBy()

        return@runCatching getTaskHistoryList(query.toString(), filter)
    }

    override fun getYesterdayHistory(filter: TaskHistoryFilter) = runCatching {
        val query = TaskHistoryQueryBuilder()
        query.bindDefaultSelect()
        query.bindDefaultInnerJoin()
        query.bindDefaultWhere()
        query.bindWhereYesterday()
        query.bindWhereEnabled()
        query.bindWhereFilter(filter)
        query.bindWhereDateRange(filter)
        query.bindDefaultOrderBy()

        return@runCatching getTaskHistoryList(query.toString(), filter)
    }

    override fun getPreviousDaysHistory(filter: TaskHistoryFilter) = runCatching {
        val query = TaskHistoryQueryBuilder()
        query.bindDefaultSelect()
        query.bindDefaultInnerJoin()
        query.bindDefaultWhere()
        query.bindWherePreviousDays()
        query.bindWhereEnabled()
        query.bindWhereFilter(filter)
        query.bindWhereDateRange(filter)
        query.bindDefaultOrderBy()

        return@runCatching getTaskHistoryList(query.toString(), filter)
    }

    override fun getTaskHistoryByTask(taskId: Long) = runCatching {
        val query = TaskHistoryQueryBuilder()
        query.bindDefaultSelect()
        query.bindDefaultInnerJoin()
        query.bindDefaultWhere()
        query.bindWhereTaskId(taskId)
        query.bindDefaultOrderBy()

        return@runCatching getTaskHistoryList(query.toString(), null)
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

    override fun getTotalOfDoneTasks(filter: TaskHistoryFilter): Result<Int> = runCatching {
        val query = TaskHistoryQueryBuilder()
        query.bindSelectCount()
        query.bindDefaultInnerJoin()
        query.bindDefaultWhere()
        query.bindWhereDateRange(filter)
        query.bindWhereTaskDone()
        query.bindWhereFilter(filter)

        return@runCatching getCountFromQuery(query.toString(), filter)
    }

    override fun getTotalOfNotDoneTasks(filter: TaskHistoryFilter): Result<Int> = runCatching {
        val query = TaskHistoryQueryBuilder()
        query.bindSelectCount()
        query.bindDefaultInnerJoin()
        query.bindDefaultWhere()
        query.bindWhereDateRange(filter)
        query.bindWhereTaskNotDone()
        query.bindWhereFilter(filter)

        return@runCatching getCountFromQuery(query.toString(), filter)
    }

    fun getTotalOfDoneTasks(taskId: Long): Result<Int> = runCatching {
        val query = TaskHistoryQueryBuilder()
        query.bindSelectCount()
        query.bindDefaultInnerJoin()
        query.bindDefaultWhere()
        query.bindWhereTaskId(taskId)
        query.bindWhereTaskDone()

        return@runCatching getCountFromQuery(query.toString())
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
            } while (cursor.isAfterLast.not())
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
