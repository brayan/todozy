package br.com.sailboat.todozy.di

import org.koin.core.module.Module

val appComponent: List<Module> = listOf(uiModule, dataModule, domainModule)