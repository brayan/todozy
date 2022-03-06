package br.com.sailboat.todozy.feature.settings.impl.domain.usecase

import br.com.sailboat.todozy.feature.settings.impl.domain.repository.SettingsRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetAlarmSoundSettingTest {

    private val repository: SettingsRepository = mockk(relaxed = true)

    private val getAlarmSoundSetting = GetAlarmSoundSetting(repository)

    @Test
    fun `should get alarm sound setting from repository`() = runBlocking {
        getAlarmSoundSetting()

        coVerify { repository.getAlarmTone() }
        confirmVerified(repository)
    }

}