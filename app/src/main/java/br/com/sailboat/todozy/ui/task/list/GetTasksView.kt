package br.com.sailboat.todozy.ui.task.list

import android.content.Context
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.domain.model.Task
import br.com.sailboat.todozy.domain.model.TaskCategory
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
        //        val types = listOf(TaskCategory.BEFORE_TODAY, TaskCategory.TODAY, TaskCategory.TOMORROW, TaskCategory.NEXT_DAYS)
//
//        val typesMap = mapOf(TaskCategory.BEFORE_TODAY to R.string.previous_days,
//                TaskCategory.TODAY to R.string.today,
//                TaskCategory.TOMORROW to R.string.tomorrow,
//                TaskCategory.NEXT_DAYS to R.string.next_days)
//
//        typesMap.map {
//            async(Dispatchers.Default) {
//                filter.category = it.key
//                getTasksView(filter, it.value)
//            }
//
//        }.awaitAll().flatten()


        val tasksView = ArrayList<ItemView>()

        filter.category = TaskCategory.BEFORE_TODAY
        addTasks(getTasks(filter), tasksView, context.getString(R.string.previous_days))

        filter.category = TaskCategory.TODAY
        addTasks(getTasks(filter), tasksView, context.getString(R.string.today))

        filter.category = TaskCategory.TOMORROW
        addTasks(getTasks(filter), tasksView, context.getString(R.string.tomorrow))

        filter.category = TaskCategory.NEXT_DAYS
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