package br.com.sailboat.todozy.core.presentation.viewholder

import android.text.TextUtils
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import br.com.sailboat.todozy.BR
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.extensions.isCurrentYear
import br.com.sailboat.todozy.core.extensions.isToday
import br.com.sailboat.todozy.core.extensions.isYesterday
import br.com.sailboat.todozy.core.extensions.toDateTimeCalendar
import br.com.sailboat.todozy.core.presentation.base.BaseViewHolderDataBinding
import br.com.sailboat.todozy.core.presentation.helper.*
import br.com.sailboat.todozy.core.presentation.model.TaskHistoryView
import br.com.sailboat.todozy.core.presentation.model.TaskStatusView
import kotlinx.android.synthetic.main.vh_task_history.view.*
import java.text.ParseException
import java.util.*

class TaskHistoryViewHolder(parent: ViewGroup, val callback: Callback) :
        BaseViewHolderDataBinding<TaskHistoryView>(inflate(parent, R.layout.vh_task_history)) {

    init {
        itemView.setOnClickListener { callback.onClickHistory(adapterPosition) }
        itemView.tvDelete.setOnClickListener { callback.onClickDelete(adapterPosition) }
    }

    interface Callback {
        fun onClickMarkTaskAsDone(position: Int)
        fun onClickMarkTaskAsNotDone(position: Int)
        fun onClickHistory(position: Int)
        fun isShowingOptions(position: Int): Boolean
        fun onClickDelete(position: Int)
    }


    override fun bind(item: TaskHistoryView) = with(itemView) {
        viewDataBiding.setVariable(BR.history, item)
//        vh_task_history__tv__name.text = item.taskName
        setStatus(item)
        setOptions(item)
        setDateTimeTask(item)
    }

    private fun setStatus(history: TaskHistoryView) = with(itemView) {
        if (isTaskDone(history)) {
            ivStatus.setImageResource(R.drawable.ic_vec_thumb_up_white_24dp)
            ivStatus.setBackgroundResource(R.drawable.shape_circle_done_task)

        } else {
            ivStatus.setImageResource(R.drawable.ic_vect_thumb_down_white_24dp)
            ivStatus.setBackgroundResource(R.drawable.shape_circle_not_done)
        }
    }

    private fun setDateTimeTask(history: TaskHistoryView) = with(itemView) {
        try {
            val calendar = history.insertingDate.toDateTimeCalendar()
            tvLongDateTime.text = getFullDateTime(calendar)
            tvShortDateTime.text = getShortDateTime(calendar)
        } catch (e: ParseException) {
            e.printStackTrace()
            tvShortDateTime.gone()
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
            tvShortDateTime.gone()
            tvLongDateTime.visible()
            llTaskHistoryActions.visible()
            tvTaskName.maxLines = Integer.MAX_VALUE
            tvTaskName.ellipsize = null
            (itemView as CardView).cardElevation = 6f

            if (isTaskDone(history)) {
                initViewMarkAsNotDone()
            } else {
                initViewMarkAsDone()
            }

        } else {
            tvShortDateTime.visible()
            tvLongDateTime.gone()
            llTaskHistoryActions.gone()
            tvTaskName.maxLines = 3
            tvTaskName.ellipsize = TextUtils.TruncateAt.END
            (itemView as CardView).cardElevation = 0f
        }
    }

    private fun initViewMarkAsNotDone() = with(itemView) {
        tvMarkAsDone.gone()
        tvMarkAsNotDone.visible()
        tvMarkAsNotDone.setOnClickListener { callback.onClickMarkTaskAsNotDone(adapterPosition) }
    }

    private fun initViewMarkAsDone() = with(itemView) {
        tvMarkAsNotDone.gone()
        tvMarkAsDone.visible()
        tvMarkAsDone.setOnClickListener { callback.onClickMarkTaskAsDone(adapterPosition) }
    }


}