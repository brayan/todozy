package br.com.sailboat.todozy.di

import android.content.Context
import br.com.sailboat.todozy.feature.about.impl.di.aboutComponent
import br.com.sailboat.todozy.feature.alarm.impl.di.alarmComponent
import br.com.sailboat.todozy.feature.settings.impl.di.settingsComponent
import br.com.sailboat.todozy.feature.task.details.impl.di.taskDetailsComponent
import br.com.sailboat.todozy.feature.task.form.impl.di.taskFormComponent
import br.com.sailboat.todozy.feature.task.history.impl.di.taskHistoryComponent
import br.com.sailboat.todozy.feature.task.list.impl.di.taskListComponent
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.check.checkModules

internal class AppComponentTest {

    @Before
    fun setUp() {
        startKoin {}
    }

    @Test
    fun `should check dependencies from AppComponent`() {
        koinApplication {
            modules(module { single { mockk<Context>() } })
            modules(appComponent)
            modules(aboutComponent)
            modules(settingsComponent)
            modules(alarmComponent)
            modules(taskHistoryComponent)
            modules(taskFormComponent)
            modules(taskListComponent)
            modules(taskDetailsComponent)
        }.checkModules()
    }
}
