package br.com.sailboat.todozy.uicomponent.viewholder

import android.text.TextUtils
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import br.com.sailboat.todozy.uicomponent.R
import br.com.sailboat.todozy.uicomponent.databinding.VhTaskHistoryBinding
import br.com.sailboat.todozy.uicomponent.model.TaskHistoryUiModel
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
        binding.tvTaskHistoryDelete.setOnClickListener {
            callback.onClickDelete(bindingAdapterPosition)
        }
    }

    override fun bind(item: TaskHistoryUiModel) = with(binding) {
        tvTaskHistoryName.text = item.taskName
        setStatus(item)
        setOptions(item)
        setDateTimeTask(item)
    }

    private fun setStatus(history: TaskHistoryUiModel) = with(binding) {
        if (history.done) {
            ivTaskHistoryStatus.setImageResource(R.drawable.ic_vec_thumb_up_white_24dp)
            ivTaskHistoryStatus.setBackgroundResource(R.drawable.shape_circle_done_task)

        } else {
            ivTaskHistoryStatus.setImageResource(R.drawable.ic_vect_thumb_down_white_24dp)
            ivTaskHistoryStatus.setBackgroundResource(R.drawable.shape_circle_not_done)
        }
    }

    private fun setDateTimeTask(history: TaskHistoryUiModel) = with(binding) {
        try {
            val calendar = history.insertingDate.toDateTimeCalendar()
            tvTaskHistoryLongDateTime.text = getFullDateTime(calendar)
            tvTaskHistoryShortDateTime.text = getShortDateTime(calendar)
        } catch (e: ParseException) {
            e.printStackTrace()
            tvTaskHistoryShortDateTime.gone()
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

    private fun setOptions(history: TaskHistoryUiModel) = with(binding) {
        if (callback.isShowingOptions(bindingAdapterPosition)) {
            tvTaskHistoryShortDateTime.gone()
            tvTaskHistoryLongDateTime.visible()
            llTaskHistoryActions.visible()
            tvTaskHistoryName.maxLines = Integer.MAX_VALUE
            tvTaskHistoryName.ellipsize = null
            (itemView as CardView).cardElevation = 6f

            if (history.done) {
                initViewMarkAsNotDone()
            } else {
                initViewMarkAsDone()
            }

        } else {
            tvTaskHistoryShortDateTime.visible()
            tvTaskHistoryLongDateTime.gone()
            llTaskHistoryActions.gone()
            tvTaskHistoryName.maxLines = 3
            tvTaskHistoryName.ellipsize = TextUtils.TruncateAt.END
            (itemView as CardView).cardElevation = 0f
        }
    }

    private fun initViewMarkAsNotDone() = with(binding) {
        tvTaskHistoryMarkAsDone.gone()
        tvTaskHistoryMarkAsNotDone.visible()
        tvTaskHistoryMarkAsNotDone.setOnClickListener {
            callback.onClickMarkTaskAsNotDone(bindingAdapterPosition)
        }
    }

    private fun initViewMarkAsDone() = with(binding) {
        tvTaskHistoryMarkAsNotDone.gone()
        tvTaskHistoryMarkAsDone.visible()
        tvTaskHistoryMarkAsDone.setOnClickListener {
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