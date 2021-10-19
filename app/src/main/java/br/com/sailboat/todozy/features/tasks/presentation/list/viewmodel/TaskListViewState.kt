package br.com.sailboat.todozy.features.tasks.presentation.list.viewmodel

import br.com.sailboat.todozy.core.presentation.helper.Event

class TaskListViewState {

    val action = Event<Action>()

    sealed class Action {
        object NavigateToAbout : Action()
        object NavigateToHistory : Action()
        object NavigateToSettings : Action()
    }

}