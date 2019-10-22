package br.com.sailboat.todozy.ui.task.list

import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.ui.base.mpv.BaseMVPContract
import br.com.sailboat.todozy.ui.model.ItemView

interface TaskListContract {

    interface View : BaseMVPContract.View {
        fun checkAndPerformFirstTimeConfigurations()
        fun closeNotifications()
        fun hideEmptyView()
        fun hideMetrics()
        fun hideTasks()
        fun removeTaskFromList(position: Int)
        fun setAlarmUpdateTasks()
        fun setMainTitle()
        fun setTitle(title: String)
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
        fun getTaskViewList(): List<ItemView>
        fun getTextForSearch(): String
        fun onClickMenuSettings()
        fun onClickMenuTaskHistory()
        fun onClickNewTask()
        fun onClickTask(position: Int)
        fun onTaskSwipedLeft(position: Int)
        fun onTaskSwipedRight(position: Int)
        fun submitTextForSearch(text: String)
    }

}