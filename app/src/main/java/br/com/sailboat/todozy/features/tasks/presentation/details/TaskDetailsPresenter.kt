package br.com.sailboat.todozy.features.tasks.presentation.details

import br.com.sailboat.todozy.core.base.Entity
import br.com.sailboat.todozy.core.presentation.base.mvp.BasePresenter
import br.com.sailboat.todozy.features.tasks.domain.model.RepeatType
import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.GetAlarm
import br.com.sailboat.todozy.features.tasks.domain.usecase.tasks.DisableTask
import br.com.sailboat.todozy.features.tasks.domain.usecase.tasks.GetTask
import br.com.sailboat.todozy.features.tasks.domain.usecase.tasks.GetTaskMetrics

class TaskDetailsPresenter(private val getTaskDetailsView: GetTaskDetailsView,
                           private val getTaskMetrics: GetTaskMetrics,
                           private val getAlarm: GetAlarm,
                           private val getTask: GetTask,
                           private val disableTask: DisableTask) :
        BasePresenter<TaskDetailsContract.View>(), TaskDetailsContract.Presenter {

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
            val task = getTask(taskId)
            disableTask(task)

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

    private fun loadDetails() = launchAsync {
        try {
            val taskId = viewModel.taskId

            val taskDetails = getTaskDetailsView(taskId)

            viewModel.taskMetrics = getTaskMetrics(TaskHistoryFilter(taskId = taskId))

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
        viewModel.taskId = view?.getTaskId() ?: Entity.NO_ID
    }

}