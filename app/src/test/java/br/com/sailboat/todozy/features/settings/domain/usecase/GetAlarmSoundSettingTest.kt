package br.com.sailboat.todozy.features.settings.domain.usecase

import br.com.sailboat.todozy.features.settings.domain.respository.SettingsRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetAlarmSoundSettingTest {

    private val repository: SettingsRepository = mockk(relaxed = true)

    private lateinit var getAlarmSoundSetting: GetAlarmSoundSetting

    @Before
    fun setUp() {
        getAlarmSoundSetting = GetAlarmSoundSetting(repository)
    }

    @Test
    fun `should get alarm sound setting from repository`() = runBlocking {
        getAlarmSoundSetting()

        coVerify { repository.getAlarmTone() }
        confirmVerified(repository)
    }

}