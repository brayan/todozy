package br.com.sailboat.todozy.features.tasks.data.datasource.local

import android.database.Cursor
import br.com.sailboat.todozy.core.base.BaseSQLite
import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.core.platform.DatabaseOpenHelper
import br.com.sailboat.todozy.features.tasks.data.model.AlarmData
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeString
import java.util.*

class AlarmLocalDataSourceSQLite(database: DatabaseOpenHelper) : BaseSQLite(database), AlarmLocalDataSource {

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

    override fun getAlarmByTask(taskId: Long): AlarmData? {
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

    override fun deleteByTask(taskId: Long) {
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

    override fun save(alarmData: AlarmData) {
        val sb = StringBuilder()

        sb.append(" INSERT INTO Alarm ")
        sb.append(" (fkTaskId, repeatType, nextAlarmDate, insertingDate, days) ")
        sb.append(" VALUES (?, ?, ?, ?, ?); ")

        val statement = compileStatement(sb.toString())
        statement.bindLong(1, alarmData.taskId)
        statement.bindLong(2, alarmData.repeatType.toLong())
        statement.bindString(3, alarmData.nextAlarmDate ?: "")
        statement.bindString(4, Calendar.getInstance().toDateTimeString())
        statement.bindString(5, alarmData.days ?: "")

        val id = insert(statement)

        alarmData.id = id
    }

    override fun update(alarmData: AlarmData) {
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
            days = getString(cursor, "days"))

}