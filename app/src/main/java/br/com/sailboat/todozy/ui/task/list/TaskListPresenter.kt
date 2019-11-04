package br.com.sailboat.todozy.ui.task.list

import br.com.sailboat.todozy.domain.alarm.GetAlarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.domain.tasks.GetTaskMetrics
import br.com.sailboat.todozy.domain.tasks.MarkTask
import br.com.sailboat.todozy.ui.base.mpv.BasePresenter
import br.com.sailboat.todozy.ui.model.TaskItemView
import kotlinx.coroutines.delay

class TaskListPresenter(private val getTasksViewUseCase: GetTasksView,
                        private val getAlarm: GetAlarm,
                        private val getTaskMetrics: GetTaskMetrics,
                        private val markTask: MarkTask)
    : BasePresenter<TaskListContract.View>(), TaskListContract.Presenter {

    private val viewModel by lazy { TaskListViewModel() }

    override fun onStart() {
        view?.checkAndPerformFirstTimeConfigurations()
        view?.setAlarmUpdateTasks()
    }

    override fun postStart() {
        loadTasks()
        updateMetrics()
    }

    override fun getTaskViewList() = viewModel.tasksView

    override fun getTextForSearch() = viewModel.filter.text

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
        val task = viewModel.tasksView[position] as TaskItemView
        view?.showTaskDetails(task.taskId)
    }

    override fun onSwipeTaskLeft(position: Int) = onTaskSwiped(position, TaskStatus.DONE)

    override fun onSwipeTaskRight(position: Int) = onTaskSwiped(position, TaskStatus.NOT_DONE)

    override fun submitTextForSearch(text: String) {
        viewModel.filter.text = text
        loadTasks() // handle the previous launched async operations?
    }

    private fun loadTasks() {
        launchAsync {
            try {
                val tasks = getTasksViewUseCase(viewModel.filter)
                viewModel.tasksView.clear()
                viewModel.tasksView.addAll(tasks)

                updateViews()

            } catch (e: Exception) {
                view?.log(e)
            }
        }
    }

    private fun onTaskSwiped(position: Int, status: TaskStatus) {
        launchAsync {
            try {
                viewModel.taskMetrics = null
                updateMetrics()

                val taskId = (viewModel.tasksView[position] as TaskItemView).taskId

                markTask(taskId, status)

                viewModel.tasksView.removeAt(position)
                view?.removeTaskFromList(position)

                val alarm = getAlarm(taskId)

                alarm?.run {
                    if (RepeatType.isAlarmRepeating(alarm)) {
                        viewModel.taskMetrics = getTaskMetrics(taskId)
                        updateMetrics()
                    }
                }

                delay(4000)

                loadTasks()

            } catch (e: Exception) {
                view?.showErrorOnSwipeTask()
                view?.log(e)
                updateViews()
            }

        }
    }

    private fun updateViews() {
        view?.closeNotifications()
        view?.updateTasks()
        updateTasksVisibility()
        updateMetrics()
    }

    private fun updateTasksVisibility() {
        if (viewModel.tasksView.isEmpty()) {
            view?.hideTasks()
            view?.showEmptyView()
        } else {
            view?.showTasks()
            view?.hideEmptyView()
        }
    }

    private fun updateMetrics() {
        if (viewModel.taskMetrics != null) {
            view?.setEmptyTitle()
            view?.showMetrics(viewModel.taskMetrics!!)
        } else {
            view?.setMainTitle()
            view?.hideMetrics()
        }
    }

}