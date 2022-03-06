package br.com.sailboat.todozy.feature.about.impl.di

import br.com.sailboat.todozy.feature.about.impl.presentation.AboutContract
import br.com.sailboat.todozy.feature.about.impl.presentation.AboutPresenter
import br.com.sailboat.todozy.feature.about.impl.presentation.navigator.AboutNavigatorImpl
import br.com.sailboat.todozy.feature.about.presentation.navigator.AboutNavigator
import org.koin.core.module.Module
import org.koin.dsl.module

private val aboutPresentationModule = module {
    factory<AboutContract.Presenter> { AboutPresenter() }
    single<AboutNavigator> { AboutNavigatorImpl() }
}

val aboutComponent: List<Module> = listOf(aboutPresentationModule)