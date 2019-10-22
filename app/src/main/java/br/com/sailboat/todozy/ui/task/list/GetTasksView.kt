package br.com.sailboat.todozy.ui.task.list

import android.content.Context
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskType
import br.com.sailboat.todozy.domain.tasks.GetTasks
import br.com.sailboat.todozy.domain.filter.TaskFilter
import br.com.sailboat.todozy.ui.mapper.mapToTaskItemView
import br.com.sailboat.todozy.ui.model.ItemView
import br.com.sailboat.todozy.ui.model.SubheadView
import br.com.sailboat.todozy.ui.model.TaskItemView
import kotlinx.coroutines.*
import java.util.*

class GetTasksView(private val context: Context,
                   private val getTasks: GetTasks) {

    suspend operator fun invoke(filter: TaskFilter): List<ItemView> = coroutineScope {
        //        val types = listOf(TaskType.BEFORE_TODAY, TaskType.TODAY, TaskType.TOMORROW, TaskType.NEXT_DAYS)
//
//        val typesMap = mapOf(TaskType.BEFORE_TODAY to R.string.previous_days,
//                TaskType.TODAY to R.string.today,
//                TaskType.TOMORROW to R.string.tomorrow,
//                TaskType.NEXT_DAYS to R.string.next_days)
//
//        typesMap.map {
//            async(Dispatchers.Default) {
//                filter.type = it.key
//                getTasksView(filter, it.value)
//            }
//
//        }.awaitAll().flatten()


        val tasksView = ArrayList<ItemView>()

        filter.type = TaskType.BEFORE_TODAY
        addTasks(getTasks(filter), tasksView, context.getString(R.string.previous_days))

        filter.type = TaskType.TODAY
        addTasks(getTasks(filter), tasksView, context.getString(R.string.today))

        filter.type = TaskType.TOMORROW
        addTasks(getTasks(filter), tasksView, context.getString(R.string.tomorrow))

        filter.type = TaskType.NEXT_DAYS
        addTasks(getTasks(filter), tasksView, context.getString(R.string.next_days))

        tasksView
    }

    private suspend fun getTasksView(filter: TaskFilter, subhead: Int): List<TaskItemView> {

        val tasks = getTasks(filter)

//        if (tasks.isNotEmpty()) {
//            tasksView.add(SubheadView(subhead))
//            tasksView.addAll(tasks.mapToTaskItemView())
//            listOf<ItemView>()
//        }

        return emptyList()
    }

    private fun addTasks(tasks: List<Task>, tasksView: MutableList<ItemView>, subhead: String) {
        if (tasks.isNotEmpty()) {
            tasksView.add(SubheadView(subhead))
            tasksView.addAll(tasks.mapToTaskItemView())
        }
    }

}