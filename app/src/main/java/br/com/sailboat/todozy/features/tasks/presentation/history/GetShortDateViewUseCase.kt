package br.com.sailboat.todozy.features.tasks.presentation.history

import java.util.*

// TODO: Repalce this use case by a service
interface GetShortDateViewUseCase {
    operator fun invoke(calendar: Calendar): String
}