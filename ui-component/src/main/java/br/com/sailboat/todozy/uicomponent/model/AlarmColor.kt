package br.com.sailboat.todozy.uicomponent.model

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.uicomponent.R
import br.com.sailboat.todozy.utility.kotlin.extension.isBeforeNow
import br.com.sailboat.todozy.utility.kotlin.extension.isToday
import br.com.sailboat.todozy.utility.kotlin.extension.isTomorrow
import br.com.sailboat.todozy.utility.kotlin.extension.isTrue
import java.util.*

class AlarmColor {

    fun getAlarmColor(context: Context, alarm: Calendar?): Int {
        try {

            if (alarm?.isBeforeNow().isTrue()) {
                return ContextCompat.getColor(context, R.color.triggered_alarm_color)
            }

            if (alarm?.isToday().isTrue()) {
                return ContextCompat.getColor(context, R.color.md_teal_300)
            }

            return if (alarm?.isTomorrow().isTrue()) {
                ContextCompat.getColor(context, R.color.md_blue_300)
            } else ContextCompat.getColor(context, R.color.md_blue_500)

        } catch (e: Exception) {
            Log.e("ALARM_COLOR", "Error while getting alarm color", e)
            return ContextCompat.getColor(context, R.color.md_blue_500)
        }

    }
}