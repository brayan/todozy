package br.com.sailboat.todozy.features.tasks.presentation.history

import android.content.Context
import br.com.sailboat.todozy.core.presentation.helper.toShortDateView
import java.util.*

// TODO: Repalce this use case by a service
class GetShortDateView(
    private val context: Context,
) : GetShortDateViewUseCase {

    override operator fun invoke(calendar: Calendar) = calendar.toShortDateView(context)

}