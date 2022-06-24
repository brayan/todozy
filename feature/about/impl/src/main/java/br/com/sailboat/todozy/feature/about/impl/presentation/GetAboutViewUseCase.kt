package br.com.sailboat.todozy.feature.about.impl.presentation

import br.com.sailboat.uicomponent.model.UiModel

internal interface GetAboutViewUseCase {
    suspend operator fun invoke(): Result<List<UiModel>>
}
