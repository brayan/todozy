package br.com.sailboat.todozy.feature.about.impl.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.todozy.uicomponent.helper.Event
import br.com.sailboat.todozy.uicomponent.model.UiModel

class AboutViewState {
    val action = Event<Action>()
    val itemViews = MutableLiveData<List<UiModel>>()

    sealed class Action {
        object ShowErrorLoadingAbout : Action()
    }
}