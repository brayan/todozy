package br.com.sailboat.todozy.platform.impl.database.table

object AlarmTable {
    const val table =
        " CREATE TABLE Alarm ( " +
            " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            " fkTaskId INTEGER, " +
            " repeatType INTEGER, " +
            " nextAlarmDate TEXT NOT NULL, " +
            " insertingDate TEXT, " +
            " days TEXT, " +
            " FOREIGN KEY(fkTaskId) REFERENCES Task(id) " +
            " ); "
}
