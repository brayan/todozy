package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.core.platform.AlarmManagerServiceImpl
import br.com.sailboat.todozy.core.platform.DatabaseOpenHelper
import br.com.sailboat.todozy.feature.alarm.impl.domain.service.AlarmManagerService
import br.com.sailboat.todozy.utility.android.sqlite.DatabaseOpenHelperService
import org.koin.core.module.Module
import org.koin.dsl.module

private val dataModule = module {
    single<DatabaseOpenHelperService> { DatabaseOpenHelper(get()) }
    factory<AlarmManagerService> { AlarmManagerServiceImpl(get()) }
}

internal val appModule: List<Module> = listOf(dataModule)
