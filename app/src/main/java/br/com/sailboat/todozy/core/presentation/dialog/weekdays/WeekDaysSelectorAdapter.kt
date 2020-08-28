package br.com.sailboat.todozy.core.presentation.dialog.weekdays

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.core.presentation.model.DayView

class WeekDaysSelectorAdapter(private val callback: Callback) : RecyclerView.Adapter<WeekDayViewHolder>() {

    interface Callback : WeekDayViewHolder.Callback {
        val days: List<DayView>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekDayViewHolder {
        return WeekDayViewHolder(parent, callback)
    }

    override fun onBindViewHolder(holder: WeekDayViewHolder, position: Int) {
        val day = callback.days[position]
        holder.bind(day)
    }

    override fun getItemCount() = callback.days.size

}