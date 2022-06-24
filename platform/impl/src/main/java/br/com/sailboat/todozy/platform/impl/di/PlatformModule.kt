package br.com.sailboat.todozy.platform.impl.di

import br.com.sailboat.todozy.platform.impl.LogServiceImpl
import br.com.sailboat.todozy.platform.impl.StringProviderImpl
import br.com.sailboat.todozy.utility.kotlin.LogService
import br.com.sailboat.todozy.utility.kotlin.StringProvider
import org.koin.dsl.module

val platformModule = listOf(
    module {
        factory<LogService> { LogServiceImpl() }
        factory<StringProvider> { StringProviderImpl(get()) }
    }
)
