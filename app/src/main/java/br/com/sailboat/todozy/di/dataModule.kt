package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.core.platform.*
import br.com.sailboat.todozy.feature.alarm.domain.service.AlarmManagerService
import br.com.sailboat.todozy.features.tasks.data.datasource.local.*
import br.com.sailboat.todozy.features.tasks.data.repository.TaskRepositoryImpl
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository
import br.com.sailboat.todozy.utility.android.sqlite.DatabaseOpenHelperService
import org.koin.dsl.module

val dataModule = module {
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }

    single<TaskLocalDataSource> { TaskLocalDataSourceSQLite(get()) }

    single<DatabaseOpenHelperService> { DatabaseOpenHelper(get(), get()) }
    single<DatabaseService> { DatabaseServiceImpl() }
    single<AlarmManagerService> { AlarmManagerServiceImpl(get()) }
}