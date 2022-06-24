package br.com.sailboat.todozy.platform.impl.database

import br.com.sailboat.todozy.platform.impl.database.table.AlarmTable
import br.com.sailboat.todozy.platform.impl.database.table.TaskHistoryTable
import br.com.sailboat.todozy.platform.impl.database.table.TaskTable
import br.com.sailboat.todozy.utility.kotlin.DatabaseTableFactory

class DatabaseTableFactoryImpl : DatabaseTableFactory {

    override fun getTables(): List<String> {
        return listOf(
            TaskTable.table,
            TaskHistoryTable.table,
            AlarmTable.table,
        )
    }
}
