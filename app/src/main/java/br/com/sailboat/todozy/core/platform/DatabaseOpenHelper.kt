package br.com.sailboat.todozy.core.platform

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.com.sailboat.todozy.feature.alarm.data.datasource.AlarmLocalDataSourceSQLite
import br.com.sailboat.todozy.feature.task.history.data.datasource.TaskHistoryLocalDataSourceSQLite
import br.com.sailboat.todozy.feature.task.list.impl.data.datasource.TaskLocalDataSourceSQLite
import br.com.sailboat.todozy.utility.android.sqlite.DatabaseOpenHelperService

class DatabaseOpenHelper(
    context: Context,
    private val databaseService: DatabaseService,
) : DatabaseOpenHelperService, SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "todozy.db"
    }

    override val readable: SQLiteDatabase
        get() = this.readableDatabase

    override val writable: SQLiteDatabase
        get() = this.writableDatabase

    override fun onCreate(db: SQLiteDatabase) {
        val tables = listOf(
            TaskLocalDataSourceSQLite(this),
            AlarmLocalDataSourceSQLite(this),
            TaskHistoryLocalDataSourceSQLite(this)
        )

        databaseService.createTables(db, tables)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}