package br.com.sailboat.todozy.feature.settings.impl.domain.usecase

import br.com.sailboat.todozy.feature.alarm.domain.usecase.ScheduleAlarmUpdatesUseCase
import br.com.sailboat.todozy.feature.settings.impl.domain.repository.SettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

internal class CheckAndSetUpInitialSettingsTest {

    private val repository: SettingsRepository = mockk(relaxed = true)
    private val scheduleAlarmUpdatesUseCase: ScheduleAlarmUpdatesUseCase = mockk(relaxed = true)

    private val checkAndSetUpInitialSettings = CheckAndSetUpInitialSettings(
        repository = repository,
        scheduleAlarmUpdatesUseCase = scheduleAlarmUpdatesUseCase,
    )

    @Test
    fun `should check if it is the first time that the app is launching from repository`() =
        runBlocking {
            coEvery { repository.isFirstTimeLaunchingApp() } returns true

            checkAndSetUpInitialSettings()

            coVerify { repository.isFirstTimeLaunchingApp() }
        }

    @Test
    fun `should set first time launching app as false when checking from repository`() =
        runBlocking {
            coEvery { repository.isFirstTimeLaunchingApp() } returns true

            checkAndSetUpInitialSettings()

            coVerify { repository.setFirstTimeLaunchingApp(false) }
        }

    @Test
    fun `should set alarm vibrate as true when it is the first time that the app is launching from repository`() =
        runBlocking {
            coEvery { repository.isFirstTimeLaunchingApp() } returns true

            checkAndSetUpInitialSettings()

            coVerify { repository.setAlarmVibrate(true) }
        }

    @Test
    fun `should set default alarm when it is the first time that the app is launching from repository`() =
        runBlocking {
            coEvery { repository.isFirstTimeLaunchingApp() } returns true

            checkAndSetUpInitialSettings()

            coVerify { repository.setDefaultAlarmTone() }
        }

    @Test
    fun `should schedule alarm update when it is the first time that the app is launching`() =
        runBlocking {
            coEvery { repository.isFirstTimeLaunchingApp() } returns true

            checkAndSetUpInitialSettings()

            coVerify { scheduleAlarmUpdatesUseCase() }
            confirmVerified(scheduleAlarmUpdatesUseCase)
        }

    @Test
    fun `should not perform any settings when isFirstTimeLaunchingApp returns false`() =
        runBlocking {
            coEvery { repository.isFirstTimeLaunchingApp() } returns false

            checkAndSetUpInitialSettings()

            coVerify(exactly = 1) { repository.isFirstTimeLaunchingApp() }
            coVerify(exactly = 0) { repository.setFirstTimeLaunchingApp(false) }
            coVerify(exactly = 0) { repository.setAlarmVibrate(true) }
            coVerify(exactly = 0) { repository.setDefaultAlarmTone() }
            coVerify(exactly = 0) { scheduleAlarmUpdatesUseCase() }
            confirmVerified(repository)
            confirmVerified(scheduleAlarmUpdatesUseCase)
        }
}
