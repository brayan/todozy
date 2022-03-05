package br.com.sailboat.todozy.feature.task.details.impl.presentation

import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.feature.task.details.presentation.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetAlarmUseCase
import br.com.sailboat.todozy.utility.android.mvp.BasePresenter
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import kotlinx.coroutines.withContext

class TaskDetailsPresenter(
    private val getTaskDetailsViewUseCase: GetTaskDetailsViewUseCase,
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase,
    private val getAlarmUseCase: GetAlarmUseCase,
    private val getTaskUseCase: GetTaskUseCase,
    private val disableTaskUseCase: DisableTaskUseCase,
) : BasePresenter<TaskDetailsContract.View>(), TaskDetailsContract.Presenter {

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

    override fun onClickDeleteTask() = launchMain {
        try {
            view?.showProgress()
            val taskId = viewModel.taskId
            val task = getTaskUseCase(taskId)
            disableTaskUseCase(task)

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

    private fun loadDetails() = launchMain {
        try {
            val taskId = viewModel.taskId

            val taskDetails = withContext(contextProvider.io) {
                getTaskDetailsViewUseCase(taskId)
            }

            viewModel.taskMetrics = withContext(contextProvider.io) {
                getTaskMetricsUseCase(TaskHistoryFilter(taskId = taskId))
            }

            viewModel.alarm = withContext(contextProvider.io) { getAlarmUseCase(taskId) }

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