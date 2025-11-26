package br.com.sailboat.uicomponent.impl.helper

import br.com.sailboat.todozy.utility.kotlin.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CoroutinesTestRule(
    val dispatcher: TestDispatcher = StandardTestDispatcher(TestCoroutineScheduler()),
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
