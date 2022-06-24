package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.core.platform.AlarmManagerServiceImpl
import br.com.sailboat.todozy.core.platform.DatabaseOpenHelper
import br.com.sailboat.todozy.core.platform.DatabaseService
import br.com.sailboat.todozy.core.platform.DatabaseServiceImpl
import br.com.sailboat.todozy.core.platform.LogServiceImpl
import br.com.sailboat.todozy.core.platform.StringProviderImpl
import br.com.sailboat.todozy.domain.service.LogService
import br.com.sailboat.todozy.feature.alarm.impl.domain.service.AlarmManagerService
import br.com.sailboat.todozy.utility.android.sqlite.DatabaseOpenHelperService
import br.com.sailboat.todozy.utility.android.string.StringProvider
import org.koin.core.module.Module
import org.koin.dsl.module

private val dataModule = module {
    single<DatabaseOpenHelperService> { DatabaseOpenHelper(get(), get()) }
    factory<DatabaseService> { DatabaseServiceImpl() }
    factory<AlarmManagerService> { AlarmManagerServiceImpl(get()) }
}

private val platformModule = module {
    factory<LogService> { LogServiceImpl() }
    factory<StringProvider> { StringProviderImpl(get()) }
}

internal val appModule: List<Module> = listOf(dataModule, platformModule)
