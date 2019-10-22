package br.com.sailboat.todozy.ui.helper

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Point
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

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

    requestedOrientation = if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
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

fun LinearLayoutManager.scrollPostionToMiddleScreen(ctx: Context, position: Int) {
    val wm = ctx.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay

    val point = Point()
    display.getSize(point)

    scrollToPositionWithOffset(position, point.y / 4)
}

fun LinearLayoutManager.scrollToTop() {
    scrollToPositionWithOffset(0, 0)
}

fun Toolbar.disableScroll() {
    val params = layoutParams as AppBarLayout.LayoutParams
    params.scrollFlags = 0
}

fun Toolbar.enableScroll() {
    val params = layoutParams as AppBarLayout.LayoutParams
    params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
}

fun RecyclerView.hideFabWhenScrolling(fab: FloatingActionButton) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0 && fab.isShown) {
                fab.hide()
            } else {
                fab.show()
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }
    })
}

fun View.scaleUp() {
    scaleX = 1f
    scaleY = 1f
}

fun View.scaleDown() {
    scaleX = 0f
    scaleY = 0f
}

fun Exception.log() {
    val tag = "ERROR_LOG"
    Log.e(tag, message ?: tag, this)
}