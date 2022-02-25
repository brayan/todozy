package br.com.sailboat.todozy.utility.android.sqlite

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteStatement
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeCalendar
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeString
import br.com.sailboat.todozy.utility.kotlin.model.BaseFilter
import java.util.*

abstract class BaseSQLite(database: SQLiteOpenHelper) {

    private val readable: SQLiteDatabase by lazy { database.readableDatabase }
    private val writable: SQLiteDatabase by lazy { database.writableDatabase }

    abstract val createTableStatement: String

    protected fun insert(statement: SQLiteStatement): Long {
        try {
            writable.beginTransactionNonExclusive()
            val id = statement.executeInsert()
            statement.clearBindings()
            writable.setTransactionSuccessful()

            return id
        } finally {
            writable.endTransaction()
        }
    }

    protected fun update(statement: SQLiteStatement) = executeUpdateOrDelete(statement)

    protected fun delete(statement: SQLiteStatement) = executeUpdateOrDelete(statement)

    private fun executeUpdateOrDelete(statement: SQLiteStatement) {
        try {
            writable.beginTransactionNonExclusive()
            statement.executeUpdateDelete()
            statement.clearBindings()
            writable.setTransactionSuccessful()
        } finally {
            writable.endTransaction()
        }
    }

    @Throws(SQLiteException::class)
    protected fun getCountFromQuery(query: String): Int {
        return getCountFromQuery(query, null)
    }

    @Throws(SQLiteException::class)
    protected fun getCountFromQuery(query: String, filter: BaseFilter?): Int {

        val cursor = if (filter != null && filter.text.isNotEmpty()) {
            performQuery(query, filter)
        } else {
            performQuery(query)
        }

        val count = cursor.count
        cursor.close()

        return count
    }

    protected fun performQuery(query: String): Cursor {
        return performQuery(query, null)
    }

    protected fun performQuery(query: String, filter: BaseFilter?): Cursor {
        return if (filter != null && filter.text.isNotEmpty()) {
            readable.rawQuery(query, arrayOf("%" + filter.text.trim { it <= ' ' } + "%"))
        } else {
            readable.rawQuery(query, null)
        }
    }

    protected fun compileStatement(statement: String): SQLiteStatement {
        return writable.compileStatement(statement)
    }

    protected fun getInt(cursor: Cursor, columnName: String): Int? {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName))
    }

    protected fun getLong(cursor: Cursor, columnName: String): Long? {
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName))
    }

    protected fun getDouble(cursor: Cursor, columnName: String): Double? {
        return cursor.getDouble(cursor.getColumnIndexOrThrow(columnName))
    }

    protected fun getString(cursor: Cursor, columnName: String): String? {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName))
    }

    protected fun getBoolean(cursor: Cursor, columnName: String): Boolean? {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName)) == 1
    }

    protected fun parseCalendarToString(date: Calendar): String? {
        return date.toDateTimeString()
    }

    protected fun parseStringToCalendar(date: String): Calendar {
        return date.toDateTimeCalendar()
    }

    protected fun getCalendarToBind(calendar: Calendar): String? {
        try {
            return parseCalendarToString(calendar)

        } catch (e: Exception) {
            return ""
        }

    }

    protected fun parseBooleanToInt(value: Boolean): Int {
        return if (value) 1 else 0
    }


}