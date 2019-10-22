package br.com.sailboat.todozy.ui.dialog.week_days

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.ui.base.BaseDialogFragment
import br.com.sailboat.todozy.ui.model.DayView
import kotlinx.android.synthetic.main.dlg_week_days_selector.view.*
import java.util.*

class WeekDaysSelectorDialog(private val callback: Callback) : BaseDialogFragment(), WeekDaysSelectorAdapter.Callback {

    private var selectedDays: String? = null
    private val hashSelectedDays = mutableMapOf<Int, DayView>()
    private var recycler: RecyclerView? = null
    private var loadedDays = mutableListOf<DayView>()
    override val days = loadedDays

    companion object {
        fun show(manager: FragmentManager, selectedDays: String?, callback: Callback) {
            val dialog = WeekDaysSelectorDialog(callback)
            dialog.selectedDays = selectedDays
            dialog.show(manager, WeekDaysSelectorDialog::class.java.name)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDays()
        initSelectedDays()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(activity, R.layout.dlg_week_days_selector, null)
        initViews(view)
        return buildDialog(view)
    }

    private fun buildDialog(view: View): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        builder.setView(view)
        builder.setPositiveButton(android.R.string.ok) { dialog, which -> callback!!.onClickOk(getSelectedDays()) }

        builder.setNegativeButton(R.string.cancel, null)

        return builder.create()
    }

    private fun initViews(view: View) {
        view.recycler.layoutManager = GridLayoutManager(activity, 2, LinearLayoutManager.HORIZONTAL, false)
        view.recycler.adapter = WeekDaysSelectorAdapter(this)
    }

    private fun initDays() {
        days.add(DayView(Calendar.SUNDAY, getString(R.string.sunday)))
        days.add(DayView(Calendar.THURSDAY, getString(R.string.thursday)))
        days.add(DayView(Calendar.MONDAY, getString(R.string.monday)))
        days.add(DayView(Calendar.FRIDAY, getString(R.string.friday)))
        days.add(DayView(Calendar.TUESDAY, getString(R.string.tuesday)))
        days.add(DayView(Calendar.SATURDAY, getString(R.string.saturday)))
        days.add(DayView(Calendar.WEDNESDAY, getString(R.string.wednesday)))
    }

    private fun initSelectedDays() {
        if (selectedDays?.isNotEmpty() == true) {
            for (element in selectedDays!!) {
                val day = Integer.valueOf(element.toString())
                hashSelectedDays[day] = getDayViewFromId(day)
            }
        }
    }

    private fun getSelectedDays(): String {
        if (hashSelectedDays.size > 0) {
            var days = ""

            for (day in hashSelectedDays.values) {
                days += day.id
            }

            return days

        } else {
            return ""
        }
    }

    override fun isDaySelected(id: Int): Boolean {
        return hashSelectedDays.get(id) != null
    }

    override fun onClickDay(position: Int) {
        val day = days[position]

        if (isDaySelected(day.id)) {
            hashSelectedDays.remove(day.id)
        } else {
            hashSelectedDays.put(day.id, day)
        }

        recycler?.adapter?.notifyItemChanged(position)

    }

    fun getDayViewFromId(id: Int): DayView {
        when (id) {
            Calendar.SUNDAY -> {
                return DayView(id, getString(R.string.sunday))
            }
            Calendar.MONDAY -> {
                return DayView(id, getString(R.string.monday))
            }
            Calendar.TUESDAY -> {
                return DayView(id, getString(R.string.tuesday))
            }
            Calendar.WEDNESDAY -> {
                return DayView(id, getString(R.string.wednesday))
            }
            Calendar.THURSDAY -> {
                return DayView(id, getString(R.string.thursday))
            }
            Calendar.FRIDAY -> {
                return DayView(id, getString(R.string.friday))
            }
            Calendar.SATURDAY -> {
                return DayView(id, getString(R.string.saturday))
            }
            else -> {
                throw IllegalArgumentException("Invalid Day ID")
            }
        }
    }


    interface Callback {
        fun onClickOk(selectedDays: String)
    }

}