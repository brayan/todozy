package br.com.sailboat.todozy.core.presentation.helper

import android.content.Context
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.extensions.isTrue
import br.com.sailboat.todozy.core.extensions.log
import br.com.sailboat.todozy.utility.kotlin.extension.isBeforeNow
import br.com.sailboat.todozy.utility.kotlin.extension.isToday
import br.com.sailboat.todozy.utility.kotlin.extension.isTomorrow
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
            e.log()
            return ContextCompat.getColor(context, R.color.md_blue_500)
        }

    }
}