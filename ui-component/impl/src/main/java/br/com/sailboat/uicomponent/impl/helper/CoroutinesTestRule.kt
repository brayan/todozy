package br.com.sailboat.uicomponent.impl.helper

import br.com.sailboat.todozy.utility.kotlin.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CoroutinesTestRule(
    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {
    val scope = TestScope(dispatcher)

    val dispatcherProvider by lazy {
        object : DispatcherProvider {
            override fun default(): CoroutineDispatcher = dispatcher
            override fun io(): CoroutineDispatcher = dispatcher
            override fun main(): CoroutineDispatcher = dispatcher
            override fun unconfined(): CoroutineDispatcher = dispatcher
        }
    }

    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
