package br.com.sailboat.todozy.features.tasks.presentation.list

import br.com.sailboat.todozy.core.presentation.base.mvp.BasePresenter
import br.com.sailboat.todozy.core.presentation.model.ItemView
import br.com.sailboat.todozy.core.presentation.model.TaskItemView
import br.com.sailboat.todozy.features.tasks.domain.model.*
import br.com.sailboat.todozy.features.tasks.domain.usecase.CompleteTaskUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.GetTaskMetricsUseCase
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.GetAlarm
import br.com.sailboat.todozy.features.tasks.domain.usecase.alarm.ScheduleAllAlarms
import kotlinx.coroutines.*

class TaskListPresenter(
    private val getTasksView: GetTasksView,
    private val getAlarm: GetAlarm,
    private val scheduleAllAlarms: ScheduleAllAlarms,
    private val getTaskMetricsUseCase: GetTaskMetricsUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
) : BasePresenter<TaskListContract.View>(), TaskListContract.Presenter {

    private var taskMetrics: TaskMetrics? = null
    private var filter = TaskFilter(TaskCategory.TODAY)
    private val swipeTaskAsyncJobs: MutableList<Job> = mutableListOf()

    override val tasksView = mutableListOf<ItemView>()

    override fun postStart() {
        loadTasks()
        scheduleAlarms()
        updateMetrics()
    }

    override fun getTextForSearch() = filter.text

    override fun onClickMenuSettings() {
        view?.showSettings()
    }

    override fun onClickMenuTaskHistory() {
        view?.showTaskHistory()
    }

    override fun onClickMenuAbout() {
        view?.navigateToAbout()
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
        launchMain {
            try {
                view?.showProgress()
                val tasks = withContext(contextProvider.io) { getTasksView(filter.text) }
                tasksView.clear()
                tasksView.addAll(tasks)
                updateViews()
            } catch (e: Exception) {
                view?.log(e)
                // TODO: HANDLE ERROR
            } finally {
                view?.hideProgress()
            }
        }
    }

    private fun scheduleAlarms() = launchMain {
        try {
            withContext(contextProvider.io) { scheduleAllAlarms() }
        } catch (e: Exception) {
            view?.log(e)
        }
    }

    private fun onTaskSwiped(position: Int, status: TaskStatus) = launchSwipeTask {
        try {
            taskMetrics = null
            updateMetrics()

            val taskId = (tasksView[position] as TaskItemView).taskId

            completeTaskUseCase(taskId, status)

            tasksView.removeAt(position)
            view?.removeTaskFromList(position)

            val alarm = getAlarm(taskId)

            alarm?.run {
                if (RepeatType.isAlarmRepeating(alarm)) {
                    taskMetrics = getTaskMetricsUseCase(TaskHistoryFilter(taskId = taskId))
                    updateMetrics()
                }
            }

            delay(4000)

            if (swipeTaskAsyncJobs.size == 1) {
                loadTasks()
                taskMetrics = null
                updateMetrics()
            }

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

    private fun launchSwipeTask(block: suspend CoroutineScope.() -> Unit) {
        val job: Job = scope.launch(contextProvider.main) {
            supervisorScope { block() }
        }
        swipeTaskAsyncJobs.add(job)
        job.invokeOnCompletion { swipeTaskAsyncJobs.remove(job) }
    }

}