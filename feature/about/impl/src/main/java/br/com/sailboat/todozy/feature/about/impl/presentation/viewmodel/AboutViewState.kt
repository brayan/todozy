package br.com.sailboat.todozy.feature.about.impl.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.uicomponent.impl.helper.Event
import br.com.sailboat.uicomponent.model.UiModel

internal class AboutViewState {
    val action = Event<Action>()
    val itemViews = MutableLiveData<List<UiModel>>()

    sealed class Action {
        object ShowErrorLoadingAbout : Action()
    }
}
