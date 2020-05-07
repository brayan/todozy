package br.com.sailboat.todozy.ui.model.view_holder

import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.domain.helper.isCurrentYear
import br.com.sailboat.todozy.domain.helper.isToday
import br.com.sailboat.todozy.domain.helper.isYesterday
import br.com.sailboat.todozy.domain.helper.toDateTimeCalendar
import br.com.sailboat.todozy.ui.base.BaseViewHolder
import br.com.sailboat.todozy.ui.helper.*
import br.com.sailboat.todozy.ui.model.TaskHistoryView
import br.com.sailboat.todozy.ui.model.TaskStatusView
import java.text.ParseException
import java.util.*
import kotlinx.android.synthetic.main.vh_task_history.view.*

class TaskHistoryViewHolder(parent: ViewGroup, val callback: Callback) :
        BaseViewHolder<TaskHistoryView>(inflate(parent, R.layout.vh_task_history)) {


    init {
        itemView.setOnClickListener { callback.onClickHistory(adapterPosition) }
        itemView.setOnLongClickListener { callback.checkIfTaskDisabled(adapterPosition) }
        itemView.vh_task_history__tv__delete.setOnClickListener { callback.onClickDelete(adapterPosition) }
    }

    interface Callback {
        fun onClickMarkTaskAsDone(position: Int)
        fun onClickMarkTaskAsNotDone(position: Int)
        fun onClickHistory(position: Int)
        fun isShowingOptions(position: Int): Boolean
        fun onClickDelete(position: Int)
        fun checkIfTaskDisabled(position: Int): Boolean
    }


    override fun bind(item: TaskHistoryView) = with(itemView) {
        vh_task_history__tv__name.text = item.taskName
        setStatus(item)
        setOptions(item)
        setDateTimeTask(item)
    }

    private fun setStatus(history: TaskHistoryView) = with(itemView) {
        if (isTaskDone(history)) {
            vh_task_history__iv__status.setImageResource(R.drawable.ic_thumb_up_white_24dp)
            vh_task_history__iv__status.setBackgroundResource(R.drawable.shape_circle_done_task)

        } else {
            vh_task_history__iv__status.setImageResource(R.drawable.ic_thumb_down_white_24dp)
            vh_task_history__iv__status.setBackgroundResource(R.drawable.shape_circle_not_done)
        }
    }

    private fun setDateTimeTask(history: TaskHistoryView) = with(itemView) {
        try {
            val calendar = history.insertingDate.toDateTimeCalendar()
            vh_task_history__tv__date_time_full.text = getFullDateTime(calendar)
            vh_task_history__tv__date_time_short.text = getShortDateTime(calendar)
        } catch (e: ParseException) {
            e.printStackTrace()
            vh_task_history__tv__date_time_short.visibility = View.GONE
        }

    }

    private fun getFullDateTime(insertingDate: Calendar): String {
        val formattedTime = insertingDate.formatTimeWithAndroidFormat(itemView.context)
        val formattedDate = insertingDate.getFullDateName(itemView.context)

        return "$formattedTime - $formattedDate"
    }

    private fun getShortDateTime(insertingDate: Calendar): String {
        return if (insertingDate.isToday() || insertingDate.isYesterday()) {
            insertingDate.formatTimeWithAndroidFormat(itemView.context)

        } else if (insertingDate.isCurrentYear()) {
            insertingDate.getMonthAndDayShort(itemView.context)

        } else {
            insertingDate.getFullDateName(itemView.context)
        }
    }

    private fun isTaskDone(history: TaskHistoryView): Boolean {
        return history.status === TaskStatusView.DONE
    }

    private fun setOptions(history: TaskHistoryView) = with(itemView) {
        if (callback.isShowingOptions(adapterPosition)) {
            vh_task_history__tv__date_time_short.visibility = View.GONE
            vh_task_history__tv__date_time_full.visibility = View.VISIBLE
            vh_task_history__ll__options.visibility = View.VISIBLE
            vh_task_history__tv__name.maxLines = Integer.MAX_VALUE
            vh_task_history__tv__name.ellipsize = null
            (itemView as CardView).cardElevation = 6f

            if (isTaskDone(history)) {
                initViewMarkAsNotDone()
            } else {
                initViewMarkAsDone()
            }

        } else {
            vh_task_history__tv__date_time_short.visibility = View.VISIBLE
            vh_task_history__tv__date_time_full.visibility = View.GONE
            vh_task_history__ll__options.visibility = View.GONE
            vh_task_history__tv__name.maxLines = 3
            vh_task_history__tv__name.ellipsize = TextUtils.TruncateAt.END
            (itemView as CardView).cardElevation = 0f
        }
    }

    private fun initViewMarkAsNotDone() = with(itemView) {
        vh_task_history__tv__mark_as_done.gone()
        vh_task_history__tv__mask_as_not_done.visible()
        vh_task_history__tv__mask_as_not_done.setOnClickListener { callback.onClickMarkTaskAsNotDone(getAdapterPosition()) }
    }

    private fun initViewMarkAsDone() = with(itemView) {
        vh_task_history__tv__mask_as_not_done.gone()
        vh_task_history__tv__mark_as_done.visible()
        vh_task_history__tv__mark_as_done.setOnClickListener { callback.onClickMarkTaskAsDone(getAdapterPosition()) }
    }


}