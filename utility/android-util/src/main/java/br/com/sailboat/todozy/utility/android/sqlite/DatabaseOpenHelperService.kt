package br.com.sailboat.todozy.utility.android.sqlite

import android.database.sqlite.SQLiteDatabase

interface DatabaseOpenHelperService {
    val readable: SQLiteDatabase
    val writable: SQLiteDatabase
}