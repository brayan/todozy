package br.com.sailboat.todozy.core.presentation.viewholder

import android.text.TextUtils
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.model.TaskHistoryUiModel
import br.com.sailboat.todozy.core.presentation.model.TaskStatusView
import br.com.sailboat.todozy.uicomponent.databinding.VhTaskHistoryBinding
import br.com.sailboat.todozy.utility.android.calendar.formatTimeWithAndroidFormat
import br.com.sailboat.todozy.utility.android.calendar.getFullDateName
import br.com.sailboat.todozy.utility.android.calendar.getMonthAndDayShort
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.todozy.utility.kotlin.extension.isCurrentYear
import br.com.sailboat.todozy.utility.kotlin.extension.isToday
import br.com.sailboat.todozy.utility.kotlin.extension.isYesterday
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeCalendar
import java.text.ParseException
import java.util.*

class TaskHistoryViewHolder(parent: ViewGroup, val callback: Callback) :
    BaseViewHolder<TaskHistoryUiModel, VhTaskHistoryBinding>(
        VhTaskHistoryBinding.inflate(getInflater(parent), parent, false)
    ) {

    init {
        binding.root.setOnClickListener { callback.onClickHistory(bindingAdapterPosition) }
        binding.tvDelete.setOnClickListener { callback.onClickDelete(bindingAdapterPosition) }
    }

    override fun bind(item: TaskHistoryUiModel) = with(binding) {
        tvTaskName.text = item.taskName
        setStatus(item)
        setOptions(item)
        setDateTimeTask(item)
    }

    private fun setStatus(history: TaskHistoryUiModel) = with(binding) {
        if (isTaskDone(history)) {
            ivStatus.setImageResource(R.drawable.ic_vec_thumb_up_white_24dp)
            ivStatus.setBackgroundResource(R.drawable.shape_circle_done_task)

        } else {
            ivStatus.setImageResource(R.drawable.ic_vect_thumb_down_white_24dp)
            ivStatus.setBackgroundResource(R.drawable.shape_circle_not_done)
        }
    }

    private fun setDateTimeTask(history: TaskHistoryUiModel) = with(binding) {
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

    private fun isTaskDone(history: TaskHistoryUiModel): Boolean {
        return history.status === TaskStatusView.DONE
    }

    private fun setOptions(history: TaskHistoryUiModel) = with(binding) {
        if (callback.isShowingOptions(bindingAdapterPosition)) {
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

    private fun initViewMarkAsNotDone() = with(binding) {
        tvMarkAsDone.gone()
        tvMarkAsNotDone.visible()
        tvMarkAsNotDone.setOnClickListener {
            callback.onClickMarkTaskAsNotDone(bindingAdapterPosition)
        }
    }

    private fun initViewMarkAsDone() = with(binding) {
        tvMarkAsNotDone.gone()
        tvMarkAsDone.visible()
        tvMarkAsDone.setOnClickListener {
            callback.onClickMarkTaskAsDone(bindingAdapterPosition)
        }
    }

    interface Callback {
        fun onClickMarkTaskAsDone(position: Int)
        fun onClickMarkTaskAsNotDone(position: Int)
        fun onClickHistory(position: Int)
        fun isShowingOptions(position: Int): Boolean
        fun onClickDelete(position: Int)
    }

}