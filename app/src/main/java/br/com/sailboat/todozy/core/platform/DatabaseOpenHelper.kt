package br.com.sailboat.todozy.core.platform

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.com.sailboat.todozy.features.tasks.data.datasource.local.AlarmLocalDataSourceSQLite
import br.com.sailboat.todozy.features.tasks.data.datasource.local.TaskHistoryLocalDataSourceSQLite
import br.com.sailboat.todozy.features.tasks.data.datasource.local.TaskLocalDataSourceSQLite

class DatabaseOpenHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "todozy.db"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val tables = listOf(
            TaskLocalDataSourceSQLite(this),
            AlarmLocalDataSourceSQLite(this),
            TaskHistoryLocalDataSourceSQLite(this)
        )

        CreateTablesHelper(db, tables).invoke()
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}