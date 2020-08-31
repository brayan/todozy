package br.com.sailboat.todozy.features.tasks.presentation.list

import br.com.sailboat.todozy.core.presentation.base.mvp.BaseMVPContract
import br.com.sailboat.todozy.core.presentation.model.ItemView
import br.com.sailboat.todozy.features.tasks.domain.model.TaskMetrics

interface TaskListContract {

    interface View : BaseMVPContract.View {
        fun checkAndPerformFirstTimeConfigurations()
        fun closeNotifications()
        fun hideEmptyView()
        fun hideMetrics()
        fun hideTasks()
        fun removeTaskFromList(position: Int)
        fun setEmptyTitle()
        fun setMainTitle()
        fun showEmptyView()
        fun showErrorOnSwipeTask()
        fun showMetrics(taskMetrics: TaskMetrics)
        fun showNewTask()
        fun showSettings()
        fun showTasks()
        fun showTaskDetails(taskId: Long)
        fun showTaskHistory()
        fun updateTasks()
    }

    interface Presenter : BaseMVPContract.Presenter {
        val tasksView: List<ItemView>
        fun getTextForSearch(): String
        fun onClickMenuSettings()
        fun onClickMenuTaskHistory()
        fun onClickNewTask()
        fun onClickTask(position: Int)
        fun onSwipeTaskLeft(position: Int)
        fun onSwipeTaskRight(position: Int)
        fun submitTextForSearch(text: String)
    }

}