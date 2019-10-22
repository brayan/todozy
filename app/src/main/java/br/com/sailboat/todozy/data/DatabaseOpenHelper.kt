package br.com.sailboat.todozy.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.com.sailboat.todozy.data.base.BaseSQLite
import br.com.sailboat.todozy.data.helper.CreateTablesHelper
import br.com.sailboat.todozy.data.sqlite.AlarmSQLite
import br.com.sailboat.todozy.data.sqlite.TaskHistorySQLite
import br.com.sailboat.todozy.data.sqlite.TaskSQLite
import java.io.DataOutput
import java.util.ArrayList

class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "todozy.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val tables = listOf(TaskSQLite(this),
                AlarmSQLite(this),
                TaskHistorySQLite(this))

        CreateTablesHelper(db, tables).invoke()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}