package br.com.sailboat.todozy.utility.android.viewmodel

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<VS, VI> : ViewModel() {
    abstract val viewState: VS
    abstract fun dispatchViewIntent(viewIntent: VI)
}
