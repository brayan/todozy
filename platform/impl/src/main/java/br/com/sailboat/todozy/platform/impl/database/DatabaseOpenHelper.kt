package br.com.sailboat.todozy.platform.impl.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import br.com.sailboat.todozy.utility.android.sqlite.DatabaseOpenHelperService
import br.com.sailboat.todozy.utility.kotlin.DatabaseTableFactory
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private const val DATABASE_VERSION = 3
private const val DATABASE_NAME = "todozy.db"

internal class DatabaseOpenHelper(
    context: Context,
) : DatabaseOpenHelperService,
    KoinComponent,
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION,
    ) {
    private val databaseTableFactory: DatabaseTableFactory by inject()

    override val readable: SQLiteDatabase
        get() = this.readableDatabase

    override val writable: SQLiteDatabase
        get() = this.writableDatabase

    override fun onCreate(db: SQLiteDatabase) {
        val tables = databaseTableFactory.getTables()

        for (table in tables) {
            db.execSQL(table)
        }
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int,
    ) {}
}
