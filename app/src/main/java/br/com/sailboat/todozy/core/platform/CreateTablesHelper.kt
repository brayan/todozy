package br.com.sailboat.todozy.core.platform

import android.database.sqlite.SQLiteDatabase
import br.com.sailboat.todozy.utility.android.sqlite.BaseSQLite

class CreateTablesHelper(
    private val database: SQLiteDatabase,
    private val tables: List<BaseSQLite>
) {

    operator fun invoke() {
        for (BaseSQLiteTable in tables) {
            database.execSQL(BaseSQLiteTable.createTableStatement)
        }
    }

}