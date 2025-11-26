package br.com.sailboat.uicomponent.impl.progress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.domain.model.TaskProgressDay
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.uicomponent.impl.databinding.ItemTaskProgressBinding

class TaskProgressHeaderAdapter(
    private val onRangeSelected: (TaskProgressRange) -> Unit,
    private val onDayClick: ((TaskProgressDay) -> Unit)? = null,
) : RecyclerView.Adapter<TaskProgressHeaderAdapter.ViewHolder>() {
    private var days: List<TaskProgressDay> = emptyList()
    private var range: TaskProgressRange = TaskProgressRange.LAST_YEAR
    private var isLoading: Boolean = false

    fun submit(
        days: List<TaskProgressDay>,
        range: TaskProgressRange,
        isLoading: Boolean = false,
    ) {
        this.days = days
        this.range = range
        this.isLoading = isLoading
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int =
        if (days.isEmpty() && isLoading.not()) {
            0
        } else {
            1
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTaskProgressBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(days, range, isLoading, onRangeSelected, onDayClick)
    }

    class ViewHolder(
        private val binding: ItemTaskProgressBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            days: List<TaskProgressDay>,
            range: TaskProgressRange,
            isLoading: Boolean,
            onRangeSelected: (TaskProgressRange) -> Unit,
            onDayClick: ((TaskProgressDay) -> Unit)?,
        ) {
            binding.taskProgress.render(
                days = days,
                range = range,
                onRangeSelected = onRangeSelected,
                onDayClick = onDayClick,
                isLoading = isLoading,
                enableDayDetails = true,
            )
        }
    }
}
