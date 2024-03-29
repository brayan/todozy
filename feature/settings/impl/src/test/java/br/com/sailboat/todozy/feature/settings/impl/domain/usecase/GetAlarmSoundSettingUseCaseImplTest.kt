package br.com.sailboat.todozy.feature.settings.impl.domain.usecase

import br.com.sailboat.todozy.feature.settings.impl.domain.repository.SettingsRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class GetAlarmSoundSettingUseCaseImplTest {

    private val repository: SettingsRepository = mockk(relaxed = true)

    private val getAlarmSoundSettingUseCase = GetAlarmSoundSettingUseCaseImpl(repository)

    @Test
    fun `should get alarm sound setting from repository`() = runBlocking {
        getAlarmSoundSettingUseCase()

        coVerify { repository.getAlarmTone() }
        confirmVerified(repository)
    }
}
