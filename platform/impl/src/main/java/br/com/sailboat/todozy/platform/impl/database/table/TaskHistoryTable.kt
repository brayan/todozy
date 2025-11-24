package br.com.sailboat.todozy.platform.impl.database.table

object TaskHistoryTable {
    const val TABLE =
        " CREATE TABLE TaskHistory ( " +
            " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " fkTaskId INTEGER, " +
            " status INTEGER, " +
            " insertingDate TEXT, " +
            " enabled INTEGER, " +
            " FOREIGN KEY(fkTaskId) REFERENCES Task(id) " +
            " ); "
}
