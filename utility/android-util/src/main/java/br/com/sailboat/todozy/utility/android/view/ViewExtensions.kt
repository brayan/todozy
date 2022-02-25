package br.com.sailboat.todozy.utility.android.view

import android.content.Context
import android.graphics.Point
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

fun LinearLayoutManager.scrollPositionToMiddleScreen(ctx: Context, position: Int) {
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
    params.scrollFlags =
        AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
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