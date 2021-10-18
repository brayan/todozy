package br.com.sailboat.todozy.features.settings.domain.usecase

import br.com.sailboat.todozy.features.settings.domain.respository.SettingsRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SetAlarmVibrateSettingTest {

    private val repository: SettingsRepository = mockk(relaxed = true)

    private val setAlarmVibrateSetting = SetAlarmVibrateSetting(repository)

    @Test
    fun `should get alarm vibrate setting from repository`() = runBlocking {
        setAlarmVibrateSetting(true)

        coVerify { repository.setAlarmVibrate(true) }
        confirmVerified(repository)
    }
}