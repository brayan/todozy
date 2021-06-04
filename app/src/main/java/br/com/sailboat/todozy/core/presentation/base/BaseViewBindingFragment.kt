package br.com.sailboat.todozy.core.presentation.base

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.core.extensions.logDebug
import java.util.*

abstract class BaseViewBindingFragment<VB : ViewBinding> : Fragment() {

    private var searchViewOpen = false
    private var search = ""

    private var _binding: ViewBinding? = null

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        "${javaClass.simpleName}.onCreate".logDebug()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        "${javaClass.simpleName}.onCreateView".logDebug()
        _binding = getBindingInflater(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        "${javaClass.simpleName}.onViewCreated".logDebug()
        initViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        "${javaClass.simpleName}.onDestroyView".logDebug()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        initSearchViewMenu(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun initSearchViewMenu(menu: Menu) {
        menu.findItem(R.id.menu_search)?.run {
            "${this@BaseViewBindingFragment.javaClass.simpleName}.initSearchViewMenu".logDebug()
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

    abstract fun getBindingInflater(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): VB

    protected open fun initViews() {}

    protected open fun onSubmitSearch(search: String) {}

}