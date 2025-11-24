package br.com.sailboat.todozy.feature.splash.impl.di

import br.com.sailboat.todozy.feature.navigation.android.SplashNavigator
import br.com.sailboat.todozy.feature.splash.impl.presentation.navigator.SplashNavigatorImpl
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation =
    module {
        factory<SplashNavigator> { SplashNavigatorImpl() }
    }

val splashModule: List<Module> = listOf(presentation)
