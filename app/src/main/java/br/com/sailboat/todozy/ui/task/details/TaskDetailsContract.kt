package br.com.sailboat.todozy.ui.task.details

import br.com.sailboat.todozy.ui.base.mpv.BaseMVPContract
import br.com.sailboat.todozy.ui.model.ItemView

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
        fun showProgress()
        fun closeProgress()
        fun showErrorOnDeleteTask()
        fun setTaskDetailsTitle()
        fun setEmptyTitle()
        fun getTaskId(): Long
    }

    interface Presenter : BaseMVPContract.Presenter {
        val details: List<ItemView>
        fun onClickMenuDelete()
        fun onClickMenuHistory()
        fun onClickDeleteTask()
        fun onClickEditTask()
    }

}