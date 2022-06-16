package br.com.sailboat.todozy.core.platform

import android.database.sqlite.SQLiteDatabase
import br.com.sailboat.todozy.utility.android.sqlite.BaseSQLite

interface DatabaseService {
    fun createTables(database: SQLiteDatabase, tables: List<BaseSQLite>)
}
