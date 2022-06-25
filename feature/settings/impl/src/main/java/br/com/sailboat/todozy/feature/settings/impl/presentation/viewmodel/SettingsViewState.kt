package br.com.sailboat.todozy.feature.settings.impl.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import br.com.sailboat.todozy.utility.android.livedata.Event

internal class SettingsViewState {

    val action = Event<Action>()
    val alarmSound = MutableLiveData<Uri>()
    val vibrate = MutableLiveData<Boolean>()

    sealed class Action {
        data class NavigateToAlarmToneSelector(val uri: Uri?) : Action()
        object NavigateToAbout : Action()
    }
}
