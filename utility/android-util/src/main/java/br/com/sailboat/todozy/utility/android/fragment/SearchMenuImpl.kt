package br.com.sailboat.todozy.utility.android.fragment

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import br.com.sailboat.todozy.utility.android.R
import java.util.Timer
import java.util.TimerTask

class SearchMenuImpl : SearchMenu {
    private var searchViewOpen = false
    private var search = ""
    private var onSubmitSearch: ((String) -> Unit)? = null

    override fun addSearchMenu(
        menu: Menu,
        onSubmitSearch: (String) -> Unit,
    ) {
        menu.findItem(R.id.menu_search)?.run {
            this@SearchMenuImpl.onSubmitSearch = onSubmitSearch
            val searchView = this.actionView as SearchView
            initListenerSearchView(searchView)
            updateSearchView(searchView, this)
        }
    }

    private fun initListenerSearchView(searchView: SearchView) {
        searchView.setOnSearchClickListener { searchViewOpen = true }

        searchView.setOnCloseListener {
            searchViewOpen = false
            false
        }

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                private var timer = Timer()
                private val DELAY: Long = 300

                override fun onQueryTextChange(text: String): Boolean {
                    timer.cancel()
                    timer = Timer()
                    timer.schedule(
                        object : TimerTask() {
                            override fun run() {
                                search = text
                                onSubmitSearch?.invoke(search)
                            }
                        },
                        DELAY,
                    )
                    return true
                }

                override fun onQueryTextSubmit(text: String): Boolean {
                    return true
                }
            },
        )
    }

    private fun updateSearchView(
        searchView: SearchView,
        menuSearchView: MenuItem,
    ) {
        if (searchViewOpen) {
            menuSearchView.expandActionView()
            searchView.setQuery(search, true)
            searchView.clearFocus()
            searchView.isFocusable = true
            searchView.isIconified = false
            searchView.requestFocusFromTouch()
        }
    }
}
