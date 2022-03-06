package br.com.sailboat.todozy.feature.task.history.impl.presentation

import java.util.*

// TODO: Repalce this use case by a service
interface GetShortDateViewUseCase {
    operator fun invoke(calendar: Calendar): String
}