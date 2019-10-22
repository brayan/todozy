package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.data.DatabaseOpenHelper
import br.com.sailboat.todozy.data.repository.AlarmRepositoryImpl
import br.com.sailboat.todozy.data.repository.TaskHistoryRepositoryImpl
import br.com.sailboat.todozy.data.repository.TaskRepositoryImpl
import br.com.sailboat.todozy.data.repository.mock.TaskRepositoryMock
import br.com.sailboat.todozy.domain.repository.AlarmRepository
import br.com.sailboat.todozy.domain.repository.TaskHistoryRepository
import br.com.sailboat.todozy.domain.repository.TaskRepository
import br.com.sailboat.todozy.domain.tasks.SaveTask
import org.koin.dsl.module

val dataModule = module {
    single<TaskRepository> { TaskRepositoryMock() }
    single<AlarmRepository> { AlarmRepositoryImpl(get(), get()) }
    single<TaskHistoryRepository> { TaskHistoryRepositoryImpl(get()) }
    single { DatabaseOpenHelper(get()) }
}