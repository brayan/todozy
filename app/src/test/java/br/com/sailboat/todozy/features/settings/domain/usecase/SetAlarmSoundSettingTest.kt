package br.com.sailboat.todozy.features.settings.domain.usecase

import android.net.Uri
import br.com.sailboat.todozy.features.settings.domain.respository.SettingsRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class SetAlarmSoundSettingTest {

    private val repository: SettingsRepository = mockk(relaxed = true)

    private val setAlarmSoundSetting = SetAlarmSoundSetting(repository)

    @Test
    fun `should set alarm sound setting from repository`() = runBlocking {
        val uriMock = mockk<Uri>()
        setAlarmSoundSetting(uriMock)

        coVerify { repository.setAlarmTone(uriMock) }
        confirmVerified(repository)
    }
}