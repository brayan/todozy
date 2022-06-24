package br.com.sailboat.todozy.feature.alarm.impl.data.datasource

import android.database.Cursor
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.feature.alarm.impl.data.model.AlarmData
import br.com.sailboat.todozy.utility.android.sqlite.BaseSQLite
import br.com.sailboat.todozy.utility.android.sqlite.DatabaseOpenHelperService
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeString
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import java.util.Calendar

internal class AlarmLocalDataSourceSQLite(
    database: DatabaseOpenHelperService,
) : BaseSQLite(database), AlarmLocalDataSource {

    override fun getAlarmByTask(taskId: Long): Result<AlarmData?> = runCatching {
        val sb = StringBuilder()
        sb.append(" SELECT Alarm.* FROM Alarm ")
        sb.append(" WHERE Alarm.fkTaskId = $taskId")

        val cursor = performQuery(sb.toString())

        if (cursor.moveToNext()) {
            val alarm = buildFromCursor(cursor)
            cursor.close()
            return@runCatching alarm
        }

        return@runCatching null
    }

    override fun deleteByTask(taskId: Long): Result<Unit?> = runCatching {
        val sql = " DELETE FROM Alarm WHERE Alarm.fkTaskId = ?"
        val statement = compileStatement(sql)
        statement.bindLong(1, taskId)

        delete(statement)
    }

    fun deleteAlarmById(alarmId: Long): Result<Unit?> = runCatching {
        val sql = " DELETE FROM Alarm WHERE Alarm.id = ?"
        val statement = compileStatement(sql)
        statement.bindLong(1, alarmId)

        delete(statement)
    }

    override fun save(alarmData: AlarmData): Result<Long> = runCatching {
        val sb = StringBuilder()

        sb.append(" INSERT INTO Alarm ")
        sb.append(" (fkTaskId, repeatType, nextAlarmDate, insertingDate, days) ")
        sb.append(" VALUES (?, ?, ?, ?, ?); ")

        val statement = compileStatement(sb.toString())
        statement.bindLong(1, alarmData.taskId)
        statement.bindLong(2, alarmData.repeatType.toLong())
        statement.bindString(3, alarmData.nextAlarmDate.orEmpty())
        statement.bindString(4, Calendar.getInstance().toDateTimeString())
        statement.bindString(5, alarmData.days.orEmpty())

        insert(statement)
    }

    override fun update(alarmData: AlarmData): Result<Unit?> = runCatching {
        val sql = StringBuilder()
        sql.append(" UPDATE Alarm SET ")
        sql.append(" repeatType = ?, ")
        sql.append(" nextAlarmDate = ?, ")
        sql.append(" days = ? ")
        sql.append(" WHERE id = ? ")

        val statement = compileStatement(sql.toString())
        statement.bindLong(1, alarmData.repeatType.toLong())
        statement.bindString(2, alarmData.nextAlarmDate ?: "")
        statement.bindString(3, alarmData.days ?: "")
        statement.bindLong(4, alarmData.id)

        update(statement)
    }

    private fun buildFromCursor(cursor: Cursor) = AlarmData(
        id = getLong(cursor, "id") ?: Entity.NO_ID,
        taskId = getLong(cursor, "fkTaskId") ?: Entity.NO_ID,
        repeatType = getInt(cursor, "repeatType") ?: RepeatType.NOT_REPEAT.ordinal,
        nextAlarmDate = getString(cursor, "nextAlarmDate"),
        insertingDate = getString(cursor, "insertingDate"),
        days = getString(cursor, "days")
    )
}
