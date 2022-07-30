package br.com.sailboat.todozy.platform.impl.di

import br.com.sailboat.todozy.platform.impl.LogServiceImpl
import br.com.sailboat.todozy.platform.impl.StringProviderImpl
import br.com.sailboat.todozy.platform.impl.database.DatabaseOpenHelper
import br.com.sailboat.todozy.platform.impl.database.DatabaseTableFactoryImpl
import br.com.sailboat.todozy.utility.android.dialog.dateselector.DateSelectorViewModel
import br.com.sailboat.todozy.utility.android.sqlite.DatabaseOpenHelperService
import br.com.sailboat.todozy.utility.kotlin.DatabaseTableFactory
import br.com.sailboat.todozy.utility.kotlin.LogService
import br.com.sailboat.todozy.utility.kotlin.StringProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val helper = module {
    factory<LogService> { LogServiceImpl() }
    factory<StringProvider> { StringProviderImpl(get()) }
    factory<DatabaseTableFactory> { DatabaseTableFactoryImpl() }
    single<DatabaseOpenHelperService> { DatabaseOpenHelper(get()) }
}

private val presentation = module {
    viewModel { DateSelectorViewModel() }
}

val platformModule: List<Module> = listOf(helper, presentation)
