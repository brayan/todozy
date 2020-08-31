package br.com.sailboat.todozy.features.tasks.presentation.list

import br.com.sailboat.todozy.core.presentation.base.mvp.BasePresenter
import br.com.sailboat.todozy.core.presentation.model.ItemView
import br.com.sailboat.todozy.core.presentation.model.TaskItemView
import br.com.sailboat.todozy.features.tasks.domain.model.*
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.GetAlarm
import br.com.sailboat.todozy.features.tasks.domain.usecase.tasks.CompleteTask
import br.com.sailboat.todozy.features.tasks.domain.usecase.tasks.GetTaskMetrics
import kotlinx.coroutines.delay

class TaskListPresenter(private val getTasksView: GetTasksView,
                        private val getAlarm: GetAlarm,
                        private val getTaskMetrics: GetTaskMetrics,
                        private val completeTask: CompleteTask)
    : BasePresenter<TaskListContract.View>(), TaskListContract.Presenter {

    private var taskMetrics: TaskMetrics? = null
    private var filter = TaskFilter(TaskCategory.TODAY)

    override val tasksView = mutableListOf<ItemView>()

    override fun onStart() {
        view?.checkAndPerformFirstTimeConfigurations()
        view?.setAlarmUpdateTasks()
    }

    override fun postStart() {
        loadTasks()
        updateMetrics()
    }

    override fun getTextForSearch() = filter.text

    override fun onClickMenuSettings() {
        view?.showSettings()
    }

    override fun onClickMenuTaskHistory() {
        view?.showTaskHistory()
    }

    override fun onClickNewTask() {
        view?.showNewTask()
    }

    override fun onClickTask(position: Int) {
        val task = tasksView[position] as TaskItemView
        view?.showTaskDetails(task.taskId)
    }

    override fun onSwipeTaskRight(position: Int) = onTaskSwiped(position, TaskStatus.DONE)

    override fun onSwipeTaskLeft(position: Int) = onTaskSwiped(position, TaskStatus.NOT_DONE)

    override fun submitTextForSearch(text: String) {
        filter.text = text
        loadTasks()
    }

    private fun loadTasks() {
        launchAsync {
            try {
                view?.showProgress()
                val tasks = getTasksView(filter)
                tasksView.clear()
                tasksView.addAll(tasks)
                updateViews()
            } catch (e: Exception) {
                view?.log(e)
            } finally {
                view?.hideProgress()
            }
        }
    }

    private fun onTaskSwiped(position: Int, status: TaskStatus) = launchAsync {
        try {
            taskMetrics = null
            updateMetrics()

            val taskId = (tasksView[position] as TaskItemView).taskId

            completeTask(taskId, status)

            tasksView.removeAt(position)
            view?.removeTaskFromList(position)

            val alarm = getAlarm(taskId)

            alarm?.run {
                if (RepeatType.isAlarmRepeating(alarm)) {
                    taskMetrics = getTaskMetrics(TaskHistoryFilter(taskId = taskId))
                    updateMetrics()
                }
            }

            delay(4000)

            loadTasks()
            taskMetrics = null
            updateMetrics()

        } catch (e: Exception) {
            view?.showErrorOnSwipeTask()
            view?.log(e)
            updateViews()
        }
    }

    private fun updateViews() = view?.run {
        closeNotifications()
        updateTasks()
        updateTasksVisibility()
        updateMetrics()
    }

    private fun updateTasksVisibility() = view?.run {
        if (tasksView.isEmpty()) {
            hideTasks()
            showEmptyView()
        } else {
            showTasks()
            hideEmptyView()
        }
    }

    private fun updateMetrics() = view?.run {
        if (taskMetrics != null) {
            setEmptyTitle()
            showMetrics(taskMetrics!!)
        } else {
            setMainTitle()
            hideMetrics()
        }
    }

}