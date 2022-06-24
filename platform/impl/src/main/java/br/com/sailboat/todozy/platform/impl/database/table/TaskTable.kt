package br.com.sailboat.todozy.platform.impl.database.table

object TaskTable {
    const val table =
        " CREATE TABLE Task ( " +
        " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        " name TEXT NOT NULL, " +
        " notes TEXT, " +
        " insertingDate TEXT, " +
        " enabled INTEGER " +
        " ); "
}
