package br.com.sailboat.todozy.features.tasks.presentation.details

import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.utility.android.mvp.BaseMVPContract

interface TaskDetailsContract {

    interface View : BaseMVPContract.View {
        fun setDoneTasks(amount: String)
        fun setNotDoneTasks(amount: String)
        fun showDialogDeleteTask()
        fun startInsertTaskActivity(taskId: Long)
        fun showMetrics()
        fun hideMetrics()
        fun startTaskHistoryActivity(taskId: Long)
        fun setFire(fire: String)
        fun showFire()
        fun hideFire()
        fun updateDetails()
        fun showErrorOnDeleteTask()
        fun setTaskDetailsTitle()
        fun setEmptyTitle()
        fun getTaskId(): Long
    }

    interface Presenter : BaseMVPContract.Presenter {
        val details: List<UiModel>
        fun onClickMenuDelete()
        fun onClickMenuHistory()
        fun onClickDeleteTask()
        fun onClickEditTask()
    }

}