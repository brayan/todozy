package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.core.platform.*
import br.com.sailboat.todozy.feature.alarm.data.datasource.AlarmLocalDataSource
import br.com.sailboat.todozy.feature.alarm.data.datasource.AlarmLocalDataSourceSQLite
import br.com.sailboat.todozy.feature.alarm.domain.service.AlarmManagerService
import br.com.sailboat.todozy.features.tasks.data.datasource.local.*
import br.com.sailboat.todozy.feature.alarm.data.mapper.AlarmDataToAlarmMapper
import br.com.sailboat.todozy.feature.alarm.data.mapper.AlarmToAlarmDataMapper
import br.com.sailboat.todozy.feature.alarm.data.repository.AlarmRepositoryImpl
import br.com.sailboat.todozy.features.tasks.data.repository.TaskHistoryRepositoryImpl
import br.com.sailboat.todozy.features.tasks.data.repository.TaskRepositoryImpl
import br.com.sailboat.todozy.feature.alarm.domain.repository.AlarmRepository
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository
import br.com.sailboat.todozy.utility.android.sqlite.DatabaseOpenHelperService
import org.koin.dsl.module

val dataModule = module {
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }

    single<TaskHistoryRepository> { TaskHistoryRepositoryImpl(get()) }

    single<TaskLocalDataSource> { TaskLocalDataSourceSQLite(get()) }

    single<TaskHistoryLocalDataSource> { TaskHistoryLocalDataSourceSQLite(get()) }

    single<DatabaseOpenHelperService> { DatabaseOpenHelper(get(), get()) }
    single<DatabaseService> { DatabaseServiceImpl() }
    single<AlarmManagerService> { AlarmManagerServiceImpl(get()) }
}