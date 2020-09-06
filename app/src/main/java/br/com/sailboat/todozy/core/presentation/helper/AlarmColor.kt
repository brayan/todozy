package br.com.sailboat.todozy.core.presentation.helper

import android.content.Context
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.extensions.*
import java.util.*

class AlarmColor {

    fun getAlarmColor(context: Context, alarm: Calendar?): Int {
        try {

            if (alarm?.isBeforeNow().safe()) {
                return ContextCompat.getColor(context, R.color.triggered_alarm_color)
            }

            if (alarm?.isToday().safe()) {
                return ContextCompat.getColor(context, R.color.md_teal_300)
            }

            return if (alarm?.isTomorrow().safe()) {
                ContextCompat.getColor(context, R.color.md_blue_300)
            } else ContextCompat.getColor(context, R.color.md_blue_500)

        } catch (e: Exception) {
            e.log()
            return ContextCompat.getColor(context, R.color.md_blue_500)
        }

    }
}