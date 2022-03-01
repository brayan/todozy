package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.core.platform.*
import br.com.sailboat.todozy.feature.alarm.domain.service.AlarmManagerService
import br.com.sailboat.todozy.utility.android.sqlite.DatabaseOpenHelperService
import org.koin.dsl.module

val dataModule = module {
    single<DatabaseOpenHelperService> { DatabaseOpenHelper(get(), get()) }
    single<DatabaseService> { DatabaseServiceImpl() }
    single<AlarmManagerService> { AlarmManagerServiceImpl(get()) }
}