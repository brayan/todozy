package br.com.sailboat.todozy.features.tasks.data.datasource.local

import br.com.sailboat.todozy.features.tasks.domain.model.TaskHistoryFilter
import br.com.sailboat.todozy.features.tasks.domain.model.TaskStatus
import br.com.sailboat.todozy.utility.kotlin.extension.*
import br.com.sailboat.todozy.utility.kotlin.model.Entity
import java.util.*

class TaskHistoryQueryBuilder {

    var query = StringBuilder()

    override fun toString(): String {
        return query.toString()
    }

    fun bindDefaultWhere(): TaskHistoryQueryBuilder {
        query.append(" WHERE 1=1 ")
        return this
    }

    fun bindWhereFilter(filter: TaskHistoryFilter?): TaskHistoryQueryBuilder {
        if (filter == null) {
            return this
        }

        if (filter.taskId != Entity.NO_ID) {
            query.append(" AND Task.id = " + filter.taskId)
        }

        if (filter.text.isNotEmpty()) {
            query.append(" AND Task.name LIKE ? ")
        }

        if (filter.status === TaskStatus.DONE) {
            query.append(" AND TaskHistory.status = 1")
        } else if (filter.status === TaskStatus.NOT_DONE) {
            query.append(" AND TaskHistory.status = 0")
        }

        return this
    }

    fun bindDefaultOrderBy(): TaskHistoryQueryBuilder {
        query.append(" ORDER BY TaskHistory.insertingDate DESC ")
        return this
    }

    fun bindWhereEnabled(): TaskHistoryQueryBuilder {
        query.append(" AND TaskHistory.enabled = 1 ")
        return this
    }

    fun bindWhereToday(): TaskHistoryQueryBuilder {
        query.append(" AND TaskHistory.insertingDate >= ")
        query.append(" '" + parseCalendarToString(getInitialCalendarForToday()) + "' ")
        return this
    }

    fun bindDefaultInnerJoin(): TaskHistoryQueryBuilder {
        query.append(" INNER JOIN Task ON (TaskHistory.fkTaskId = Task.id) ")
        return this
    }

    fun bindDefaultSelect(): TaskHistoryQueryBuilder {
        query.append(" SELECT TaskHistory.*, Task.name FROM TaskHistory ")
        return this
    }

    fun bindSelectCount(): TaskHistoryQueryBuilder {
        query.append(" SELECT 1 FROM TaskHistory ")
        return this
    }

    fun bindWhereYesterday(): TaskHistoryQueryBuilder {
        query.append(" AND (TaskHistory.insertingDate >= ")
        query.append(" '" + parseCalendarToString(getInitialCalendarForYesterday()) + "' ")
        query.append(" AND TaskHistory.insertingDate <= ")
        query.append(" '" + parseCalendarToString(getFinalCalendarForYesterday()) + "') ")
        return this
    }

    fun bindWherePreviousDays(): TaskHistoryQueryBuilder {
        query.append(" AND TaskHistory.insertingDate < ")
        query.append(" '" + parseCalendarToString(getInitialCalendarForYesterday()) + "' ")
        return this
    }

    fun bindWhereTaskId(taskId: Long): TaskHistoryQueryBuilder {
        query.append(" AND Task.id = $taskId")
        return this
    }

    fun bindWherePreviousDays(initialDate: Calendar, finalDate: Calendar): TaskHistoryQueryBuilder {
        query.append(" AND (TaskHistory.insertingDate >= ")
        query.append(" '" + parseCalendarToString(initialDate) + "' ")
        query.append(" AND TaskHistory.insertingDate <= ")
        query.append(" '" + parseCalendarToString(finalDate) + "') ")

        return this
    }

    fun bindWhereDateRange(filter: TaskHistoryFilter): TaskHistoryQueryBuilder {
        if (filter.initialDate != null && filter.finalDate != null) {
            filter.initialDate?.clearTime()
            filter.finalDate?.setFinalTimeToCalendar()

            query.append(" AND (TaskHistory.insertingDate >= ")
            query.append(" '" + filter.initialDate?.toDateTimeString() + "' ")
            query.append(" AND TaskHistory.insertingDate <= ")
            query.append(" '" + filter.finalDate?.toDateTimeString() + "') ")
        }

        return this
    }

    protected fun parseCalendarToString(date: Calendar): String {
        return date.toDateTimeString()
    }

    fun bindWhereTaskDone(): TaskHistoryQueryBuilder {
        query.append(" AND TaskHistory.status = 1 ")
        return this
    }

    fun bindWhereTaskNotDone(): TaskHistoryQueryBuilder {
        query.append(" AND TaskHistory.status = 0 ")
        return this
    }
}