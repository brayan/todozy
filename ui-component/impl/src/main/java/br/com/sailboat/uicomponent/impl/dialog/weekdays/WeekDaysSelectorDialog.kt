package br.com.sailboat.uicomponent.impl.dialog.weekdays

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.sailboat.uicomponent.impl.R
import br.com.sailboat.uicomponent.impl.databinding.DlgWeekDaysSelectorBinding
import br.com.sailboat.uicomponent.model.DayUiModel
import java.util.Calendar

class WeekDaysSelectorDialog(private val callback: Callback) :
    DialogFragment(),
    WeekDaysSelectorAdapter.Callback {
    private var selectedDays = ""
    private val hashSelectedDays = mutableMapOf<Int, DayUiModel>()
    private var loadedDays = mutableListOf<DayUiModel>()
    override val days = loadedDays

    private lateinit var binding: DlgWeekDaysSelectorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDays()
        initSelectedDays()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DlgWeekDaysSelectorBinding.inflate(LayoutInflater.from(requireContext()))
        initViews()
        return buildDialog()
    }

    private fun initViews() =
        with(binding) {
            recycler.layoutManager =
                GridLayoutManager(activity, 2, LinearLayoutManager.HORIZONTAL, false)
            recycler.adapter = WeekDaysSelectorAdapter(this@WeekDaysSelectorDialog)
        }

    private fun buildDialog(): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)
        builder.setPositiveButton(android.R.string.ok) { _, _ -> callback.onClickOk(getSelectedDays()) }

        builder.setNegativeButton(R.string.cancel, null)

        return builder.create()
    }

    private fun initDays() {
        days.add(DayUiModel(Calendar.SUNDAY, getString(R.string.sunday)))
        days.add(DayUiModel(Calendar.THURSDAY, getString(R.string.thursday)))
        days.add(DayUiModel(Calendar.MONDAY, getString(R.string.monday)))
        days.add(DayUiModel(Calendar.FRIDAY, getString(R.string.friday)))
        days.add(DayUiModel(Calendar.TUESDAY, getString(R.string.tuesday)))
        days.add(DayUiModel(Calendar.SATURDAY, getString(R.string.saturday)))
        days.add(DayUiModel(Calendar.WEDNESDAY, getString(R.string.wednesday)))
    }

    private fun initSelectedDays() {
        if (selectedDays.isNotEmpty()) {
            for (element in selectedDays) {
                val dayId = Integer.valueOf(element.toString())
                getDayViewFromId(dayId)?.let { day ->
                    hashSelectedDays[dayId] = day
                }
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

    private fun getDayViewFromId(id: Int) =
        when (id) {
            Calendar.SUNDAY -> DayUiModel(id, getString(R.string.sunday))
            Calendar.MONDAY -> DayUiModel(id, getString(R.string.monday))
            Calendar.TUESDAY -> DayUiModel(id, getString(R.string.tuesday))
            Calendar.WEDNESDAY -> DayUiModel(id, getString(R.string.wednesday))
            Calendar.THURSDAY -> DayUiModel(id, getString(R.string.thursday))
            Calendar.FRIDAY -> DayUiModel(id, getString(R.string.friday))
            Calendar.SATURDAY -> DayUiModel(id, getString(R.string.saturday))
            else -> null
        }

    interface Callback {
        fun onClickOk(selectedDays: String)
    }

    companion object {
        fun show(
            manager: FragmentManager,
            selectedDays: String?,
            callback: Callback,
        ) {
            val dialog = WeekDaysSelectorDialog(callback)
            dialog.selectedDays = selectedDays.orEmpty()
            dialog.show(manager, WeekDaysSelectorDialog::class.java.name)
        }
    }
}
