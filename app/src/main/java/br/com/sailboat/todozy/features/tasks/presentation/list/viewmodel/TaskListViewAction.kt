package br.com.sailboat.todozy.features.tasks.presentation.list.viewmodel

sealed class TaskListViewAction {
    object OnClickMenuSettings : TaskListViewAction()
    object OnClickMenuHistory : TaskListViewAction()
    object OnClickMenuAbout : TaskListViewAction()
}