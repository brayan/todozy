package br.com.sailboat.todozy.utility.android.livedata

import androidx.annotation.AnyThread
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

open class Event<T> : MutableLiveData<T>() {

    private val hasBeenHandled: AtomicBoolean = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(
            owner,
            Observer<T> {
                if (hasBeenHandled.compareAndSet(true, false)) {
                    observer.onChanged(it)
                }
            }
        )
    }

    @MainThread
    override fun setValue(value: T?) {
        hasBeenHandled.set(true)
        super.setValue(value)
    }

    @MainThread
    fun call() {
        value = value
    }

    @AnyThread
    fun postCall() {
        postValue(value)
    }
}
