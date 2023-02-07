package br.com.sailboat.todozy.feature.about.impl.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.todozy.utility.android.livedata.Event
import br.com.sailboat.uicomponent.model.UiModel

internal class AboutViewState {
    val viewAction = Event<AboutViewAction>()
    val itemViews = MutableLiveData<List<UiModel>>()
}
