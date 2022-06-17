package br.com.sailboat.todozy.feature.about.impl.di

import br.com.sailboat.todozy.feature.about.impl.presentation.GetAboutView
import br.com.sailboat.todozy.feature.about.impl.presentation.GetAboutViewUseCase
import br.com.sailboat.todozy.feature.about.impl.presentation.navigator.AboutNavigatorImpl
import br.com.sailboat.todozy.feature.about.impl.presentation.viewmodel.AboutViewModel
import br.com.sailboat.todozy.feature.navigation.android.AboutNavigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    viewModel {
        AboutViewModel(
            getAboutViewUseCase = get(),
            logService = get(),
        )
    }
    factory<GetAboutViewUseCase> { GetAboutView(get()) }
    factory<AboutNavigator> { AboutNavigatorImpl() }
}

val aboutComponent: List<Module> = listOf(presentation)
