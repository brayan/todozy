package br.com.sailboat.todozy.data.helper

import android.database.sqlite.SQLiteDatabase
import br.com.sailboat.todozy.data.base.BaseSQLite

class CreateTablesHelper(private val database: SQLiteDatabase, private val tables: List<BaseSQLite>) {

    operator fun invoke() {
        for (BaseSQLiteTable in tables) {
            database.execSQL(BaseSQLiteTable.createTableStatement)
        }
    }

}