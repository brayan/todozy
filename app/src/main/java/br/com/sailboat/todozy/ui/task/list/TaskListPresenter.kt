package br.com.sailboat.todozy.ui.task.list

import br.com.sailboat.todozy.domain.alarm.GetAlarm
import br.com.sailboat.todozy.domain.filter.TaskFilter
import br.com.sailboat.todozy.domain.filter.TaskHistoryFilter
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.TaskCategory
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.tasks.GetTaskMetrics
import br.com.sailboat.todozy.domain.tasks.MarkTask
import br.com.sailboat.todozy.ui.base.mpv.BasePresenter
import br.com.sailboat.todozy.ui.model.ItemView
import br.com.sailboat.todozy.ui.model.TaskItemView
import kotlinx.coroutines.delay

class TaskListPresenter(private val getTasksViewUseCase: GetTasksView,
                        private val getAlarm: GetAlarm,
                        private val getTaskMetrics: GetTaskMetrics,
                        private val markTask: MarkTask)
    : BasePresenter<TaskListContract.View>(), TaskListContract.Presenter {

    private val tasksView = mutableListOf<ItemView>()
    private var taskMetrics: TaskMetrics? = null
    private var filter = TaskFilter(TaskCategory.TODAY)

    override fun onStart() {
        view?.checkAndPerformFirstTimeConfigurations()
        view?.setAlarmUpdateTasks()
        loadTasks()
        updateMetrics()
    }

    override fun onRestart() {
        updateViews()
    }

    override fun getTaskViewList() = tasksView

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
        loadTasks() // handle the previous launched async operations?
    }

    private fun loadTasks() {
        launchAsync {
            try {
                view?.showProgress()
                val tasks = getTasksViewUseCase(filter)
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

            markTask(taskId, status)

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