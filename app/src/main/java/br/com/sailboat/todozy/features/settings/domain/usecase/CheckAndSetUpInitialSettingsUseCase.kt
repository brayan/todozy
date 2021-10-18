package br.com.sailboat.todozy.features.settings.domain.usecase

interface CheckAndSetUpInitialSettingsUseCase {
    suspend operator fun invoke()
}