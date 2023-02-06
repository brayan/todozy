package br.com.sailboat.todozy.feature.about.impl.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import br.com.sailboat.todozy.feature.about.impl.presentation.GetAboutViewUseCase
import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel
import br.com.sailboat.todozy.utility.kotlin.LogService
import kotlinx.coroutines.launch

internal class AboutViewModel(
    override val viewState: AboutViewState = AboutViewState(),
    private val getAboutViewUseCase: GetAboutViewUseCase,
    private val logService: LogService,
) : BaseViewModel<AboutViewState, AboutViewAction>() {

    override fun dispatchViewIntent(viewIntent: AboutViewAction) {
        when (viewIntent) {
            is AboutViewAction.OnStart -> onStart()
        }
    }

    private fun onStart() = viewModelScope.launch {
        try {
            viewState.itemViews.value = getAboutViewUseCase().getOrThrow()
        } catch (e: Exception) {
            logService.error(e)
            viewState.action.value = AboutViewState.Action.ShowErrorLoadingAbout
        }
    }
}
