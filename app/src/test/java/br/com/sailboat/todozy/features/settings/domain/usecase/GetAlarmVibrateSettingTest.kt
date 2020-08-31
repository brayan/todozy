package br.com.sailboat.todozy.features.settings.domain.usecase

import br.com.sailboat.todozy.features.settings.domain.respository.SettingsRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetAlarmVibrateSettingTest {

    private val repository: SettingsRepository = mockk(relaxed = true)

    private lateinit var getAlarmVibrateSetting: GetAlarmVibrateSetting

    @Before
    fun setUp() {
        getAlarmVibrateSetting = GetAlarmVibrateSetting(repository)
    }

    @Test
    fun `should get alarm vibrate setting from repository`() = runBlocking {
        getAlarmVibrateSetting()

        coVerify { repository.getAlarmVibrate() }
        confirmVerified(repository)
    }

}