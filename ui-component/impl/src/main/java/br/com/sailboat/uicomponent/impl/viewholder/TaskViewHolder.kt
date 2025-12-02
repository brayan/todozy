package br.com.sailboat.uicomponent.impl.viewholder

import android.util.Log
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.utility.android.calendar.formatTimeWithAndroidFormat
import br.com.sailboat.todozy.utility.android.calendar.getMonthAndDayShort
import br.com.sailboat.todozy.utility.android.calendar.toShortDateView
import br.com.sailboat.todozy.utility.android.recyclerview.BaseViewHolder
import br.com.sailboat.todozy.utility.android.view.gone
import br.com.sailboat.todozy.utility.android.view.setSafeClickListener
import br.com.sailboat.todozy.utility.android.view.visible
import br.com.sailboat.todozy.utility.kotlin.extension.isAfterTomorrow
import br.com.sailboat.todozy.utility.kotlin.extension.isBeforeToday
import br.com.sailboat.todozy.utility.kotlin.extension.isCurrentYear
import br.com.sailboat.uicomponent.impl.R
import br.com.sailboat.uicomponent.impl.databinding.VhTaskBinding
import br.com.sailboat.uicomponent.model.TaskUiModel
import java.util.Calendar

class TaskViewHolder(parent: ViewGroup, private val callback: Callback) :
    BaseViewHolder<TaskUiModel, VhTaskBinding>(
        VhTaskBinding.inflate(getInflater(parent), parent, false),
    ) {
    interface Callback {
        fun onClickTask(taskId: Long)
        fun onClickUndo(
            taskId: Long,
            status: TaskStatus,
        )
    }

    private val defaultElevation = binding.root.cardElevation
    private val inlineElevation = binding.root.resources.getDimension(R.dimen.task_inline_metrics_elevation)
    private val defaultMinHeight = binding.task.root.minimumHeight
    private val defaultBottomPadding = binding.task.root.paddingBottom
    private val defaultInlineTopMargin =
        (binding.task.inlineMetricsContainer.layoutParams as ConstraintLayout.LayoutParams).topMargin

    override fun bind(item: TaskUiModel) = with(binding) {
        task.tvTaskName.text = item.taskName
        bindTaskAlarm(item)
        root.setSafeClickListener {
            callback.onClickTask(item.taskId)
        }
        bindInlineMetrics(item)
    }

    private fun bindTaskAlarm(item: TaskUiModel) {
        try {
            updateVisibilityOfAlarmViews(item.alarm)
            item.alarm?.run {
                updateAlarmText(this)
                updateAlarmColor(item.alarmColor)
            }
        } catch (e: Exception) {
            Log.e("TASK_VIEW_HOLDER", "Error binding alarm", e)
        }
    }

    private fun updateAlarmText(alarm: Calendar) = with(binding) {
        if (alarm.isBeforeToday() || alarm.isAfterTomorrow()) {
            task.tvTaskDate.text =
                if (alarm.isCurrentYear()) {
                    alarm.getMonthAndDayShort(context)
                } else {
                    alarm.toShortDateView(context)
                }
        } else {
            task.tvTaskTime.text = alarm.formatTimeWithAndroidFormat(context)
        }
    }

    private fun updateAlarmColor(alarmColor: Int?) = with(binding) {
        alarmColor?.let {
            task.tvTaskDate.setTextColor(alarmColor)
            task.tvTaskTime.setTextColor(alarmColor)
        }
    }

    private fun updateVisibilityOfAlarmViews(alarm: Calendar?) = with(binding) {
        if (alarm == null) {
            task.tvTaskDate.gone()
            task.tvTaskTime.gone()
        } else if (alarm.isBeforeToday() || alarm.isAfterTomorrow()) {
            task.tvTaskTime.gone()
            task.tvTaskDate.visible()
        } else {
            task.tvTaskTime.visible()
            task.tvTaskDate.gone()
        }
    }

    private fun bindInlineMetrics(item: TaskUiModel) = with(binding) {
        if (item.showInlineMetrics) {
            root.cardElevation = inlineElevation
            task.root.minimumHeight = 0
            task.root.setPadding(task.root.paddingLeft, task.root.paddingTop, task.root.paddingRight, 0)
            updateInlineTopMargin(0)
            task.inlineMetricsContainer.visible()
            bindInlineMetricsValues(item)
            task.tvTaskName.gone()
            task.flTaskDateTime.gone()
            task.btnInlineUndo.setSafeClickListener {
                callback.onClickUndo(item.taskId, item.inlineStatus ?: TaskStatus.NOT_DONE)
            }
        } else {
            root.cardElevation = defaultElevation
            task.root.minimumHeight = defaultMinHeight
            task.root.setPadding(
                task.root.paddingLeft,
                task.root.paddingTop,
                task.root.paddingRight,
                defaultBottomPadding,
            )
            updateInlineTopMargin(defaultInlineTopMargin)
            task.inlineMetricsContainer.gone()
            task.tvTaskName.visible()
            task.flTaskDateTime.visible()
            task.btnInlineUndo.setOnClickListener(null)
        }
    }

    private fun updateInlineTopMargin(margin: Int) {
        val layoutParams =
            binding.task.inlineMetricsContainer.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.topMargin = margin
        binding.task.inlineMetricsContainer.layoutParams = layoutParams
    }

    private fun bindInlineMetricsValues(item: TaskUiModel) = with(binding.task.inlineTaskMetrics) {
        tvMetricsFire.text = item.inlineMetrics?.consecutiveDone?.toString().orEmpty()
        tvMetricsDone.text = item.inlineMetrics?.doneTasks?.toString().orEmpty()
        tvMetricsNotDone.text = item.inlineMetrics?.notDoneTasks?.toString().orEmpty()

        if ((item.inlineMetrics?.consecutiveDone ?: 0) == 0) {
            taskMetricsLlFire.gone()
        } else {
            taskMetricsLlFire.visible()
        }
    }
}
