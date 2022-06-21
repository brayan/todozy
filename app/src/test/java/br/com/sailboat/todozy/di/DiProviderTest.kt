package br.com.sailboat.todozy.di

import android.content.Context
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import org.koin.test.check.checkModules

class DiProviderTest {

    @Before
    fun setUp() {
        startKoin {}
    }

    @Test
    fun `should check all modules from DiProvider`() {
        koinApplication {
            modules(module { single { mockk<Context>() } })
            modules(DiProvider.getModules())
        }.checkModules()
    }
}
