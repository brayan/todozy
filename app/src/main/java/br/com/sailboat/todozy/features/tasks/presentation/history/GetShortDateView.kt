package br.com.sailboat.todozy.features.tasks.presentation.history

import android.content.Context
import br.com.sailboat.todozy.core.presentation.helper.toShortDateView
import java.util.*

class GetShortDateView(private val context: Context) {

    operator fun invoke(calendar: Calendar) = calendar.toShortDateView(context)

}