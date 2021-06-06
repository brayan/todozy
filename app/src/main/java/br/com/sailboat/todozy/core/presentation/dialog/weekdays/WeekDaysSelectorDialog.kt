package br.com.sailboat.todozy.core.presentation.dialog.weekdays

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.presentation.base.BaseDialogFragment
import br.com.sailboat.todozy.core.presentation.model.DayView
import br.com.sailboat.todozy.databinding.DlgWeekDaysSelectorBinding
import java.util.*

class WeekDaysSelectorDialog(private val callback: Callback) : BaseDialogFragment(),
    WeekDaysSelectorAdapter.Callback {

    private var selectedDays: String? = null
    private val hashSelectedDays = mutableMapOf<Int, DayView>()
    private var loadedDays = mutableListOf<DayView>()
    override val days = loadedDays

    private lateinit var binding: DlgWeekDaysSelectorBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DlgWeekDaysSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    interface Callback {
        fun onClickOk(selectedDays: String)
    }

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
        initViews()
        return buildDialog()
    }

    private fun buildDialog(): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        builder.setPositiveButton(android.R.string.ok) { _, _ -> callback.onClickOk(getSelectedDays()) }

        builder.setNegativeButton(R.string.cancel, null)

        return builder.create()
    }

    private fun initViews() = with(binding) {
        recycler.layoutManager =
            GridLayoutManager(activity, 2, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = WeekDaysSelectorAdapter(this@WeekDaysSelectorDialog)
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
        return if (hashSelectedDays.isNotEmpty()) {
            var days = ""

            for (day in hashSelectedDays.values) {
                days += day.id
            }

            days

        } else {
            ""
        }
    }

    override fun isDaySelected(id: Int): Boolean {
        return hashSelectedDays[id] != null
    }

    override fun onClickDay(position: Int) {
        val day = days[position]

        if (isDaySelected(day.id)) {
            hashSelectedDays.remove(day.id)
        } else {
            hashSelectedDays[day.id] = day
        }

        binding.recycler.adapter?.notifyItemChanged(position)

    }

    private fun getDayViewFromId(id: Int): DayView {
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

}