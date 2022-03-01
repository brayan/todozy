package br.com.sailboat.todozy.di

import br.com.sailboat.todozy.domain.service.LogService
import br.com.sailboat.todozy.core.platform.LogServiceImpl
import org.koin.dsl.module

val platformModule = module {
    factory<LogService> { LogServiceImpl() }
}