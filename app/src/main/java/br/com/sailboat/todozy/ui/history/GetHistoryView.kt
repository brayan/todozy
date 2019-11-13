package br.com.sailboat.todozy.ui.history

import android.content.Context
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.domain.filter.TaskFilter
import br.com.sailboat.todozy.domain.filter.TaskHistoryFilter
import br.com.sailboat.todozy.domain.history.GetTasksHistory
import br.com.sailboat.todozy.ui.model.ItemView

class GetHistoryView(private val context: Context,
                     private val getTasksHistory: GetTasksHistory) {

    suspend operator fun invoke(filter: TaskHistoryFilter): List<ItemView> {
        val tasks = mutableListOf<ItemView>()

        addTodayTasks(context, filter, tasks)
        addYesterdayTasks(context, filter, tasks)
        addPreviousDaysTasks(context, filter, tasks)

        return tasks
    }

    private fun addPreviousDaysTasks(ctx: Context, filter: TaskHistoryFilter?, tasks: MutableList<ItemView>) {
        checkFilterForPreviousDays(filter)

        val taskSQLite = TaskHistorySQLite.newInstance(ctx)

        if (filter != null && filter!!.getFinalDate() != null && filter!!.getInitialDate() != null) {

            val finalDate = filter!!.getFinalDate().clone() as Calendar

            if (DateHelper.isToday(finalDate)) {
                finalDate.add(Calendar.DAY_OF_MONTH, -2)
            } else if (DateHelper.isYesterday(finalDate)) {
                finalDate.add(Calendar.DAY_OF_MONTH, -1)
            }

            addTasks(taskSQLite.getPreviousDaysHistoryFromDate(filter!!.getInitialDate(), finalDate, filter), tasks, ctx.getString(R.string.previous_days))

        } else {
            addTasks(taskSQLite.getPreviousDaysHistory(filter), tasks, ctx.getString(R.string.previous_days))
        }
    }

    private fun addYesterdayTasks(ctx: Context, filter: TaskHistoryFilter, tasks: MutableList<ItemView>) {
        checkFilterForYesterday(filter)

        val taskSQLite = TaskHistorySQLite.newInstance(ctx)
        addTasks(taskSQLite.getYesterdayHistory(filter), tasks, ctx.getString(R.string.yesterday))
    }


    private fun addTodayTasks(ctx: Context, filter: TaskHistoryFilter, tasks: MutableList<ItemView>) {
        checkFilterForToday(filter)

        val taskSQLite = TaskHistorySQLite.newInstance(ctx)
        addTasks(taskSQLite.getTodayHistory(filter), tasks, ctx.getString(R.string.today))
    }

    private fun checkFilterForToday(filter: TaskHistoryFilter?) {
        if (filter == null || filter!!.getFinalDate() == null) {
            return
        }

        if (DateHelper.isNotToday(filter!!.getFinalDate())) {
            throw FilterException()
        }
    }

    private fun checkFilterForYesterday(filter: TaskHistoryFilter?) {
        if (filter == null || filter!!.getFinalDate() == null || filter!!.getInitialDate() == null) {
            return
        }

        if (DateHelper.isYesterday(filter!!.getInitialDate())) {
            return
        }

        if (DateHelper.isYesterday(filter!!.getFinalDate())) {
            return
        }

        if (DateHelper.isBeforeYesterday(filter!!.getInitialDate()) && DateHelper.isAfterYesterday(filter!!.getFinalDate())) {
            return
        }

        throw FilterException()

    }

    private fun checkFilterForPreviousDays(filter: TaskHistoryFilter?) {
        if (filter == null || filter!!.getInitialDate() == null) {
            return
        }

        if (DateHelper.isBeforeYesterday(filter!!.getInitialDate())) {
            return
        }

        throw FilterException()

    }

    private fun addTasks(fromList: List<*>, toList: MutableList<*>, subheaderText: String) {
        if (ListHelper.isNotEmpty(fromList)) {
            addSubheader(toList, subheaderText)
            toList.addAll(fromList)
        }
    }

    private fun addSubheader(tasks: MutableList<ItemView>, subheaderText: String) {
        val subheader = SubheaderView(ViewType.SUBHEADER)
        subheader.setText(subheaderText)
        tasks.add(subheader)
    }

}