package br.com.sailboat.todozy.feature.about.impl.presentation

import br.com.sailboat.todozy.uicomponent.model.UiModel

interface GetAboutViewUseCase {
    suspend operator fun invoke(): Result<List<UiModel>>
}