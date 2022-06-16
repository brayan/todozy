package br.com.sailboat.todozy.core.platform

import android.database.sqlite.SQLiteDatabase
import br.com.sailboat.todozy.utility.android.sqlite.BaseSQLite

class DatabaseServiceImpl : DatabaseService {

    override fun createTables(database: SQLiteDatabase, tables: List<BaseSQLite>) {
        for (table in tables) {
            database.execSQL(table.createTableStatement)
        }
    }
}
