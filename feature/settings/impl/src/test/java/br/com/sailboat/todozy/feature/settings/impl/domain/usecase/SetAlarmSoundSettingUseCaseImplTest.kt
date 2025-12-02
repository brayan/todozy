package br.com.sailboat.todozy.feature.settings.impl.domain.usecase

import android.net.Uri
import br.com.sailboat.todozy.feature.settings.impl.domain.repository.SettingsRepository
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class SetAlarmSoundSettingUseCaseImplTest {
    private val repository: SettingsRepository = mockk(relaxed = true)

    private val setAlarmSoundSettingUseCase = SetAlarmSoundSettingUseCaseImpl(repository)

    @Test
    fun `should set alarm sound setting from repository`() = runBlocking {
        val uriMock = mockk<Uri>()
        setAlarmSoundSettingUseCase(uriMock)

        coVerify { repository.setAlarmTone(uriMock) }
        confirmVerified(repository)
    }
}
