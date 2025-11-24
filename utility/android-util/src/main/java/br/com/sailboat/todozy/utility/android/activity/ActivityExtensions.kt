package br.com.sailboat.todozy.utility.android.activity

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun Activity.hideKeyboard() {
    currentFocus?.run { getInputManager().hideSoftInputFromWindow(windowToken, 0) }
}

fun Activity.setActivityToHideKeyboard() {
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
}

fun Activity.showKeyboard(editText: EditText) {
    getInputManager().showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.blockScreenOrientation() {
    val config = resources.configuration

    requestedOrientation =
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } else {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
}

fun Activity.unblockScreenOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}

private fun Activity.getInputManager(): InputMethodManager {
    return getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
}
