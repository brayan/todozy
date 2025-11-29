package br.com.sailboat.uicomponent.impl.progress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.domain.model.TaskProgressDay
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.uicomponent.impl.databinding.ItemTaskProgressBinding
import java.time.DayOfWeek

class TaskProgressHeaderAdapter(
    private val onRangeSelected: (TaskProgressRange) -> Unit,
    private val onDayClick: ((TaskProgressDay) -> Unit)? = null,
    private val highlightNotDone: Boolean = false,
    private val flatColors: Boolean = highlightNotDone,
) : RecyclerView.Adapter<TaskProgressHeaderAdapter.ViewHolder>() {
    private var days: List<TaskProgressDay> = emptyList()
    private var range: TaskProgressRange = TaskProgressRange.LAST_YEAR
    private var isLoading: Boolean = false
    private var visibleDaysOfWeek: List<DayOfWeek> = DefaultTaskProgressDayOrder

    fun submit(
        days: List<TaskProgressDay>,
        range: TaskProgressRange,
        isLoading: Boolean = false,
        visibleDaysOfWeek: List<DayOfWeek> = DefaultTaskProgressDayOrder,
    ) {
        this.days = days
        this.range = range
        this.isLoading = isLoading
        this.visibleDaysOfWeek = visibleDaysOfWeek.ifEmpty { DefaultTaskProgressDayOrder }
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
        holder.bind(
            days = days,
            range = range,
            isLoading = isLoading,
            onRangeSelected = onRangeSelected,
            onDayClick = onDayClick,
            visibleDaysOfWeek = visibleDaysOfWeek,
            highlightNotDone = highlightNotDone,
            flatColors = flatColors,
        )
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
            visibleDaysOfWeek: List<DayOfWeek>,
            highlightNotDone: Boolean,
            flatColors: Boolean,
        ) {
            binding.taskProgress.render(
                days = days,
                range = range,
                onRangeSelected = onRangeSelected,
                onDayClick = onDayClick,
                isLoading = isLoading,
                enableDayDetails = true,
                visibleDaysOfWeek = visibleDaysOfWeek,
                highlightNotDone = highlightNotDone,
                flatColors = flatColors,
            )
        }
    }
}
