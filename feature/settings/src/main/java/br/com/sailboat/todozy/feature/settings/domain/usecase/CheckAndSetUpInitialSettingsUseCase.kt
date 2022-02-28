package br.com.sailboat.todozy.feature.settings.domain.usecase

interface CheckAndSetUpInitialSettingsUseCase {
    suspend operator fun invoke()
}