package br.com.sailboat.todozy.utility.android.view

import android.content.Context
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

fun LinearLayoutManager.scrollPositionToMiddleScreen(
    ctx: Context,
    position: Int,
) {
    val height = ctx.resources.displayMetrics.heightPixels
    scrollToPositionWithOffset(position, height / 4)
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

fun RecyclerView.hideFabWhenScrolling(fab: ExtendedFloatingActionButton) {
    addOnScrollListener(
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int,
            ) {
                if (dy > 0 && fab.isShown) {
                    fab.hide()
                } else if (dy < 0 && fab.isShown.not()) {
                    fab.show()
                }
            }
        },
    )
}

fun View.scaleUp() {
    scaleX = 1f
    scaleY = 1f
}

fun View.scaleDown() {
    scaleX = 0f
    scaleY = 0f
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}
