package br.com.sailboat.uicomponent.impl.di

import br.com.sailboat.uicomponent.impl.dialog.selectable.SelectItemViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val presentation = module {
    viewModel { SelectItemViewModel() }
}

val uiComponentModule: List<Module> = listOf(presentation)
