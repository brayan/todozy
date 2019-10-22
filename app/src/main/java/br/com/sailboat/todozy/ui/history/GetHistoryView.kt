package br.com.sailboat.todozy.ui.history

class GetHistoryView {

//    @Throws(Exception::class)
//    fun loadHistory(context: Context, filter: TaskHistoryFilter): List<ItemView> {
//        val tasks = ArrayList<ItemView>()
//
//        addTodayTasks(context, filter, tasks)
//        addYesterdayTasks(context, filter, tasks)
//        addPreviousDaysTasks(context, filter, tasks)
//
//        return tasks
//    }
//
//    @Throws(Exception::class)
//    private fun addPreviousDaysTasks(ctx: Context, filter: TaskHistoryFilter?, tasks: MutableList<ItemView>) {
//        try {
//            checkFilterForPreviousDays(filter)
//
//            val taskSQLite = TaskHistorySQLite.newInstance(ctx)
//
//            if (filter != null && filter!!.getFinalDate() != null && filter!!.getInitialDate() != null) {
//
//                val finalDate = filter!!.getFinalDate().clone() as Calendar
//
//                if (DateHelper.isToday(finalDate)) {
//                    finalDate.add(Calendar.DAY_OF_MONTH, -2)
//                } else if (DateHelper.isYesterday(finalDate)) {
//                    finalDate.add(Calendar.DAY_OF_MONTH, -1)
//                }
//
//                addTasks(taskSQLite.getPreviousDaysHistoryFromDate(filter!!.getInitialDate(), finalDate, filter), tasks, ctx.getString(R.string.previous_days))
//
//            } else {
//                addTasks(taskSQLite.getPreviousDaysHistory(filter), tasks, ctx.getString(R.string.previous_days))
//            }
//
//        } catch (ignore: FilterException) {
//
//        } catch (e: Exception) {
//            throw e
//        }
//
//    }
//
//    @Throws(Exception::class)
//    private fun addYesterdayTasks(ctx: Context, filter: TaskHistoryFilter, tasks: MutableList<ItemView>) {
//        try {
//            checkFilterForYesterday(filter)
//
//            val taskSQLite = TaskHistorySQLite.newInstance(ctx)
//            addTasks(taskSQLite.getYesterdayHistory(filter), tasks, ctx.getString(R.string.yesterday))
//
//        } catch (ignore: FilterException) {
//
//        } catch (e: Exception) {
//            throw e
//        }
//
//    }
//
//
//    @Throws(Exception::class)
//    private fun addTodayTasks(ctx: Context, filter: TaskHistoryFilter, tasks: MutableList<ItemView>) {
//        try {
//            checkFilterForToday(filter)
//
//            val taskSQLite = TaskHistorySQLite.newInstance(ctx)
//            addTasks(taskSQLite.getTodayHistory(filter), tasks, ctx.getString(R.string.today))
//
//        } catch (ignore: FilterException) {
//
//        } catch (e: Exception) {
//            throw e
//        }
//
//    }
//
//    @Throws(FilterException::class)
//    private fun checkFilterForToday(filter: TaskHistoryFilter?) {
//        if (filter == null || filter!!.getFinalDate() == null) {
//            return
//        }
//
//        if (DateHelper.isNotToday(filter!!.getFinalDate())) {
//            throw FilterException()
//        }
//    }
//
//    @Throws(FilterException::class)
//    private fun checkFilterForYesterday(filter: TaskHistoryFilter?) {
//        if (filter == null || filter!!.getFinalDate() == null || filter!!.getInitialDate() == null) {
//            return
//        }
//
//        if (DateHelper.isYesterday(filter!!.getInitialDate())) {
//            return
//        }
//
//        if (DateHelper.isYesterday(filter!!.getFinalDate())) {
//            return
//        }
//
//        if (DateHelper.isBeforeYesterday(filter!!.getInitialDate()) && DateHelper.isAfterYesterday(filter!!.getFinalDate())) {
//            return
//        }
//
//        throw FilterException()
//
//    }
//
//    @Throws(FilterException::class)
//    private fun checkFilterForPreviousDays(filter: TaskHistoryFilter?) {
//        if (filter == null || filter!!.getInitialDate() == null) {
//            return
//        }
//
//        if (DateHelper.isBeforeYesterday(filter!!.getInitialDate())) {
//            return
//        }
//
//        throw FilterException()
//
//    }
//
//    private fun addTasks(fromList: List<*>, toList: MutableList<*>, subheaderText: String) {
//        if (ListHelper.isNotEmpty(fromList)) {
//            addSubheader(toList, subheaderText)
//            toList.addAll(fromList)
//        }
//    }
//
//    private fun addSubheader(tasks: MutableList<ItemView>, subheaderText: String) {
//        val subheader = SubheaderView(ViewType.SUBHEADER)
//        subheader.setText(subheaderText)
//        tasks.add(subheader)
//    }

}