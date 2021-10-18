package br.com.sailboat.todozy.features.tasks.presentation.history

import android.content.Context
import br.com.sailboat.todozy.core.presentation.helper.toShortDateView
import java.util.*

// TODO: Repalce this use case by a service
interface GetShortDateViewUseCase {
    operator fun invoke(calendar: Calendar): String
}