package br.com.sailboat.todozy.ui.helper

import android.content.Context
import androidx.core.content.ContextCompat
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.domain.helper.isBeforeNow
import br.com.sailboat.todozy.domain.helper.isToday
import br.com.sailboat.todozy.domain.helper.isTomorrow
import java.util.*

class AlarmColor {

    fun getAlarmColor(context: Context, alarm: Calendar?): Int {
        try {

            if (alarm?.isBeforeNow() == true) {
                return ContextCompat.getColor(context, R.color.triggered_alarm_color)
            }

            if (alarm?.isToday() == true) {
                return ContextCompat.getColor(context, R.color.md_teal_300)
            }

            return if (alarm?.isTomorrow() == true) {
                ContextCompat.getColor(context, R.color.md_blue_300)
            } else ContextCompat.getColor(context, R.color.md_blue_500)

        } catch (e: Exception) {
            //LogHelper.logException(e)
            return ContextCompat.getColor(context, R.color.md_blue_500)
        }

    }
}