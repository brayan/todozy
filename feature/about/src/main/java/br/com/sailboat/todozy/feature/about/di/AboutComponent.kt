package br.com.sailboat.todozy.feature.about.di

import br.com.sailboat.todozy.feature.about.presentation.AboutContract
import br.com.sailboat.todozy.feature.about.presentation.AboutPresenter
import org.koin.core.module.Module
import org.koin.dsl.module

private val aboutPresentationModule = module {
    factory<AboutContract.Presenter> { AboutPresenter() }
}

val aboutComponent: List<Module> = listOf(aboutPresentationModule)