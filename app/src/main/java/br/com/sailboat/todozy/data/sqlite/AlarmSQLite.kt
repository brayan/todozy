package br.com.sailboat.todozy.data.sqlite

import android.content.Context
import android.database.Cursor
import br.com.sailboat.todozy.data.DatabaseOpenHelper
import br.com.sailboat.todozy.data.base.BaseSQLite
import br.com.sailboat.todozy.data.model.AlarmData
import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.domain.helper.toDateTimeString
import br.com.sailboat.todozy.domain.model.RepeatType
import java.util.*

class AlarmSQLite(database: DatabaseOpenHelper) : BaseSQLite(database) {

    override val createTableStatement = StringBuilder()
            .append(" CREATE TABLE Alarm ( ")
            .append(" id INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(" fkTaskId INTEGER, ")
            .append(" repeatType INTEGER, ")
            .append(" nextAlarmDate TEXT NOT NULL, ")
            .append(" insertingDate TEXT, ")
            .append(" days TEXT, ")
            .append(" FOREIGN KEY(fkTaskId) REFERENCES Task(id) ")
            .append(" ); ")
            .toString()

    fun getAlarmByTask(taskId: Long): AlarmData? {
        val sb = StringBuilder()
        sb.append(" SELECT Alarm.* FROM Alarm ")
        sb.append(" WHERE Alarm.fkTaskId = $taskId")

        val cursor = performQuery(sb.toString())

        if (cursor.moveToNext()) {
            val alarm = buildFromCursor(cursor)
            cursor.close()
            return alarm
        }

        return null
    }

    fun deleteByTask(taskId: Long) {
        val sql = " DELETE FROM Alarm WHERE Alarm.fkTaskId = ?"
        val statement = compileStatement(sql)
        statement.bindLong(1, taskId)

        delete(statement)
    }

    fun deleteAlarmById(alarmId: Long) {
        val sql = " DELETE FROM Alarm WHERE Alarm.id = ?"
        val statement = compileStatement(sql)
        statement.bindLong(1, alarmId)

        delete(statement)
    }

    fun save(alarm: AlarmData) {
        val sb = StringBuilder()

        sb.append(" INSERT INTO Alarm ")
        sb.append(" (fkTaskId, repeatType, nextAlarmDate, insertingDate, days) ")
        sb.append(" VALUES (?, ?, ?, ?, ?); ")

        val statement = compileStatement(sb.toString())
        statement.bindLong(1, alarm.taskId)
        statement.bindLong(2, alarm.repeatType.toLong())
        statement.bindString(3, alarm.nextAlarmDate ?: "")
        statement.bindString(4, Calendar.getInstance().toDateTimeString())
        statement.bindString(5, alarm.days ?: "")

        val id = insert(statement)

        alarm.id = id
    }

    fun update(alarm: AlarmData) {
        val sql = StringBuilder()
        sql.append(" UPDATE Alarm SET ")
        sql.append(" repeatType = ?, ")
        sql.append(" nextAlarmDate = ?, ")
        sql.append(" days = ? ")
        sql.append(" WHERE id = ? ")

        val statement = compileStatement(sql.toString())
        statement.bindLong(1, alarm.repeatType.toLong())
        statement.bindString(2, alarm.nextAlarmDate ?: "")
        statement.bindString(3, alarm.days ?: "")
        statement.bindLong(4, alarm.id)

        update(statement)
    }

    private fun buildFromCursor(cursor: Cursor) = AlarmData(
            id = getLong(cursor, "id") ?: EntityHelper.NO_ID,
            taskId = getLong(cursor, "fkTaskId") ?: EntityHelper.NO_ID,
            repeatType = getInt(cursor, "repeatType") ?: RepeatType.NOT_REPEAT.ordinal,
            nextAlarmDate = getString(cursor, "nextAlarmDate"),
            insertingDate = getString(cursor, "insertingDate"),
            days = getString(cursor, "days"))

}