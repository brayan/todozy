package br.com.sailboat.uicomponent.impl.progress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.domain.model.TaskProgressDay
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.uicomponent.impl.databinding.ItemTaskProgressBinding

class TaskProgressHeaderAdapter(
    private val onRangeSelected: (TaskProgressRange) -> Unit,
) : RecyclerView.Adapter<TaskProgressHeaderAdapter.ViewHolder>() {
    private var days: List<TaskProgressDay> = emptyList()
    private var range: TaskProgressRange = TaskProgressRange.LAST_YEAR

    fun submit(
        days: List<TaskProgressDay>,
        range: TaskProgressRange,
    ) {
        this.days = days
        this.range = range
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = if (days.isEmpty()) 0 else 1

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
        holder.bind(days, range, onRangeSelected)
    }

    class ViewHolder(
        private val binding: ItemTaskProgressBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            days: List<TaskProgressDay>,
            range: TaskProgressRange,
            onRangeSelected: (TaskProgressRange) -> Unit,
        ) {
            binding.taskProgress.render(
                days = days,
                range = range,
                onRangeSelected = onRangeSelected,
                onDayClick = null,
            )
        }
    }
}
