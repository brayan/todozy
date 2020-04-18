package br.com.sailboat.todozy.ui.task.details

import br.com.sailboat.todozy.domain.alarm.GetAlarm
import br.com.sailboat.todozy.domain.helper.EntityHelper
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.tasks.GetTaskMetrics
import br.com.sailboat.todozy.ui.base.mpv.BasePresenter
import kotlinx.coroutines.runBlocking

class TaskDetailsPresenter(private val getTaskDetailsView: GetTaskDetailsView,
                           private val getTaskMetrics: GetTaskMetrics,
                           private val getAlarm: GetAlarm) : BasePresenter<TaskDetailsContract.View>(), TaskDetailsContract.Presenter {

    private val viewModel = TaskDetailsViewModel()
    override val details = viewModel.details

    override fun onStart() {
        extractTaskId()
        loadDetails()
    }

    override fun onRestart() {
        updateContentViews()
    }

    override fun onClickEditTask() {
        view?.startInsertTaskActivity(viewModel.taskId)
    }

    override fun onClickMenuDelete() {
        view?.showDialogDeleteTask()
    }

    override fun onClickDeleteTask() = launchAsync {
        try {
            view?.showProgress()
            val taskId = viewModel.taskId
            // TODO: DISABLETASK
            // DisableTask(taskId)

            view?.hideProgress()
            view?.closeWithResultOk()
        } catch (e: Exception) {
            view?.hideProgress()
            view?.log(e)
            view?.showErrorOnDeleteTask()
        }
    }

    override fun onClickMenuHistory() {
        view?.startTaskHistoryActivity(viewModel.taskId)
    }

    private fun hasRepeatingAlarm(): Boolean {
        return (viewModel.alarm?.repeatType != RepeatType.NOT_REPEAT)
    }

    private fun updateMetricsViews() {
        if (hasRepeatingAlarm()) {
            view?.showMetrics()
            view?.setDoneTasks(viewModel.taskMetrics.doneTasks.toString())
            view?.setNotDoneTasks(viewModel.taskMetrics.notDoneTasks.toString())
            view?.setEmptyTitle()

            if (viewModel.taskMetrics.consecutiveDone > 0) {
                view?.setFire(viewModel.taskMetrics.consecutiveDone.toString())
                view?.showFire()
            } else {
                view?.hideFire()
            }

        } else {
            view?.hideMetrics()
            view?.setEmptyTitle()
            view?.setTaskDetailsTitle()
        }
    }

    private fun loadDetails() = runBlocking {
        try {
            val taskId = viewModel.taskId

            val taskDetails = getTaskDetailsView(taskId)

            viewModel.taskMetrics = getTaskMetrics(taskId)

            viewModel.alarm = getAlarm(taskId)

            viewModel.details.clear()
            viewModel.details.addAll(taskDetails)
            updateContentViews()

        } catch (e: Exception) {
            view?.log(e)
            view?.closeWithResultNotOk()
        }

    }

    private fun updateContentViews() {
        updateMetricsViews()
        view?.updateDetails()
    }

    private fun extractTaskId() {
        viewModel.taskId = view?.getTaskId() ?: EntityHelper.NO_ID
    }

}