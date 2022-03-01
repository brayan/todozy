package br.com.sailboat.todozy.feature.settings.domain.usecase

import br.com.sailboat.todozy.feature.settings.domain.repository.SettingsRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GetAlarmVibrateSettingTest {

    private val repository: SettingsRepository = mockk(relaxed = true)

    private val getAlarmVibrateSetting = GetAlarmVibrateSetting(repository)

    @Test
    fun `should get alarm vibrate setting from repository`() = runBlocking {
        getAlarmVibrateSetting()

        coVerify { repository.getAlarmVibrate() }
        confirmVerified(repository)
    }

}