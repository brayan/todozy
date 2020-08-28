package br.com.sailboat.todozy.core.di

import br.com.sailboat.todozy.core.platform.DatabaseOpenHelper
import br.com.sailboat.todozy.features.tasks.data.datasource.local.*
import br.com.sailboat.todozy.features.tasks.data.repository.AlarmRepositoryImpl
import br.com.sailboat.todozy.features.tasks.data.repository.TaskHistoryRepositoryImpl
import br.com.sailboat.todozy.features.tasks.data.repository.TaskRepositoryImpl
import br.com.sailboat.todozy.features.tasks.domain.repository.AlarmRepository
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.features.tasks.domain.repository.TaskRepository
import org.koin.dsl.module

val dataModule = module {
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
    single<AlarmRepository> { AlarmRepositoryImpl(get(), get()) }
    single<TaskHistoryRepository> { TaskHistoryRepositoryImpl(get()) }
    single<TaskLocalDataSource> { TaskLocalDataSourceSQLite(get()) }
    single<AlarmLocalDataSource> { AlarmLocalDataSourceSQLite(get()) }
    single<TaskHistoryLocalDataSource> { TaskHistoryLocalDataSourceSQLite(get()) }
    single { DatabaseOpenHelper(get()) }
}