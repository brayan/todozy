package br.com.sailboat.uicomponent.impl.viewholder

import android.text.TextUtils
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.todozy.utility.kotlin.extension.toDateTimeCalendar
import br.com.sailboat.uicomponent.impl.R
import br.com.sailboat.uicomponent.impl.databinding.VhTaskHistoryBinding
import br.com.sailboat.uicomponent.impl.formatter.TaskHistoryDateTimeFormatter
import br.com.sailboat.uicomponent.model.TaskHistoryUiModel
import java.text.ParseException

class TaskHistoryViewHolder(
    parent: ViewGroup,
    private val formatter: TaskHistoryDateTimeFormatter,
    val callback: Callback,
) :
    BaseViewHolder<TaskHistoryUiModel, VhTaskHistoryBinding>(
            VhTaskHistoryBinding.inflate(getInflater(parent), parent, false),
        ) {
    override fun bind(item: TaskHistoryUiModel) =
        with(binding) {
            root.setOnClickListener { callback.onClickHistory(bindingAdapterPosition) }
            tvTaskHistoryDelete.setOnClickListener { callback.onClickDelete(bindingAdapterPosition) }

            tvTaskHistoryName.text = item.taskName
            setStatus(item)
            setOptions(item)
            setDateTimeTask(item)
        }

    private fun setStatus(history: TaskHistoryUiModel) =
        with(binding) {
            if (history.done) {
                ivTaskHistoryStatus.setImageResource(R.drawable.ic_vec_thumb_up_white_24dp)
                ivTaskHistoryStatus.setBackgroundResource(R.drawable.shape_circle_done_task)
            } else {
                ivTaskHistoryStatus.setImageResource(R.drawable.ic_vect_thumb_down_white_24dp)
                ivTaskHistoryStatus.setBackgroundResource(R.drawable.shape_circle_not_done)
            }
        }

    private fun setDateTimeTask(history: TaskHistoryUiModel) =
        with(binding) {
            try {
                val calendar = history.insertingDate.toDateTimeCalendar()
                tvTaskHistoryLongDateTime.text = formatter.formatFull(calendar)
                tvTaskHistoryShortDateTime.text = formatter.formatShort(calendar)
            } catch (e: ParseException) {
                e.printStackTrace()
                tvTaskHistoryShortDateTime.gone()
            }
        }

    private fun setOptions(history: TaskHistoryUiModel) =
        with(binding) {
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

    private fun initViewMarkAsNotDone() =
        with(binding) {
            tvTaskHistoryMarkAsDone.gone()
            tvTaskHistoryMarkAsNotDone.visible()
            tvTaskHistoryMarkAsNotDone.setOnClickListener { callback.onClickMarkTaskAsNotDone(bindingAdapterPosition) }
        }

    private fun initViewMarkAsDone() =
        with(binding) {
            tvTaskHistoryMarkAsNotDone.gone()
            tvTaskHistoryMarkAsDone.visible()
            tvTaskHistoryMarkAsDone.setOnClickListener { callback.onClickMarkTaskAsDone(bindingAdapterPosition) }
        }

    interface Callback {
        fun onClickMarkTaskAsDone(position: Int)
        fun onClickMarkTaskAsNotDone(position: Int)
        fun onClickHistory(position: Int)
        fun isShowingOptions(position: Int): Boolean
        fun onClickDelete(position: Int)
    }
}
