package br.com.sailboat.todozy.feature.settings.impl.domain.usecase

interface CheckAndSetUpInitialSettingsUseCase {
    suspend operator fun invoke()
}