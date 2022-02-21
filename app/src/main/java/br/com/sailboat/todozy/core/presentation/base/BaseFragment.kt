package br.com.sailboat.todozy.core.presentation.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.extensions.logDebug
import java.util.*

abstract class BaseFragment : Fragment() {

    private var searchViewOpen = false
    private var search = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        "${javaClass.simpleName}.onCreate".logDebug()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        "${javaClass.simpleName}.onViewCreated".logDebug()
        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        initSearchViewMenu(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initSearchViewMenu(menu: Menu) {
        menu.findItem(R.id.menu_search)?.run {
            "${this@BaseFragment.javaClass.simpleName}.initSearchViewMenu".logDebug()
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

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            private var timer = Timer()
            private val DELAY: Long = 500

            override fun onQueryTextChange(text: String): Boolean {
                timer.cancel()
                timer = Timer()
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        search = text
                        onSubmitSearch(search)
                    }
                }, DELAY)

                return true

            }

            override fun onQueryTextSubmit(text: String): Boolean {
                return true
            }
        })
    }

    private fun updateSearchView(searchView: SearchView, menuSearchView: MenuItem) {

        if (searchViewOpen) {
            menuSearchView.expandActionView()
            searchView.setQuery(search, true)
            searchView.clearFocus()
            searchView.isFocusable = true
            searchView.isIconified = false
            searchView.requestFocusFromTouch()
        }
    }

    protected open fun initViews() {}

    protected open fun onSubmitSearch(search: String) {}

}