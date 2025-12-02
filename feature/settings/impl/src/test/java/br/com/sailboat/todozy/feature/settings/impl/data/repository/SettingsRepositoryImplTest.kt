package br.com.sailboat.todozy.feature.settings.impl.data.repository

import android.net.Uri
import br.com.sailboat.todozy.feature.settings.impl.data.datasource.SettingsLocalDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class SettingsRepositoryImplTest {
    private val settingsLocalDataSource: SettingsLocalDataSource = mockk(relaxed = true)

    private val settingsRepository =
        SettingsRepositoryImpl(
            localDataSource = settingsLocalDataSource,
        )

    @Test
    fun `should call setAlarmTone from data source when called from repository`() = runBlocking {
        val alarmTone: Uri = mockk()
        prepareScenario()

        settingsRepository.setAlarmTone(alarmTone)

        coVerify { settingsLocalDataSource.setAlarmTone(alarmTone) }
    }

    @Test
    fun `should call setAlarmVibrate from data source when called from repository`() = runBlocking {
        prepareScenario()

        settingsRepository.setAlarmVibrate(true)

        coVerify { settingsLocalDataSource.setAlarmVibrate(true) }
    }

    @Test
    fun `should call setDefaultAlarmTone from data source when called from repository`() = runBlocking {
        prepareScenario()

        settingsRepository.setDefaultAlarmTone()

        coVerify { settingsLocalDataSource.setDefaultAlarmTone() }
    }

    @Test
    fun `should call setFirstTimeLaunchingApp from data source when called from repository`() = runBlocking {
        prepareScenario()

        settingsRepository.setFirstTimeLaunchingApp(true)

        coVerify { settingsLocalDataSource.setFirstTimeLaunchingApp(true) }
    }

    @Test
    fun `should call getAlarmTone from data source when called from repository`() = runBlocking {
        val alarmTone: Uri = mockk()
        prepareScenario(alarmTone = alarmTone)

        val result = settingsRepository.getAlarmTone()

        coVerify { settingsLocalDataSource.getAlarmTone() }
        assertEquals(alarmTone, result)
    }

    @Test
    fun `should call getAlarmVibrate from data source when called from repository`() = runBlocking {
        prepareScenario(vibrate = false)

        val result = settingsRepository.getAlarmVibrate()

        coVerify { settingsLocalDataSource.getAlarmVibrate() }
        assertFalse { result }
    }

    @Test
    fun `should call isFirstTimeLaunchingApp from data source when called from repository`() = runBlocking {
        prepareScenario(isFirstTimeLaunchingApp = true)

        val result = settingsRepository.isFirstTimeLaunchingApp()

        coVerify { settingsLocalDataSource.isFirstTimeLaunchingApp() }
        assertTrue { result }
    }

    private fun prepareScenario(
        alarmTone: Uri? = mockk(),
        vibrate: Boolean = true,
        isFirstTimeLaunchingApp: Boolean = false,
    ) {
        coEvery { settingsLocalDataSource.setAlarmTone(any()) } just runs
        coEvery { settingsLocalDataSource.getAlarmTone() } returns alarmTone
        coEvery { settingsLocalDataSource.setAlarmVibrate(any()) } just runs
        coEvery { settingsLocalDataSource.setDefaultAlarmTone() } just runs
        coEvery { settingsLocalDataSource.getAlarmVibrate() } returns vibrate
        coEvery { settingsLocalDataSource.setFirstTimeLaunchingApp(any()) } just runs
        coEvery { settingsLocalDataSource.isFirstTimeLaunchingApp() } returns isFirstTimeLaunchingApp
    }
}
