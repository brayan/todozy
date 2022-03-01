package br.com.sailboat.todozy.feature.task.history.presentation

import android.content.Context
import br.com.sailboat.todozy.utility.android.calendar.toShortDateView
import java.util.*

// TODO: Repalce this use case by a service
class GetShortDateView(
    private val context: Context,
) : GetShortDateViewUseCase {

    override operator fun invoke(calendar: Calendar) = calendar.toShortDateView(context)

}