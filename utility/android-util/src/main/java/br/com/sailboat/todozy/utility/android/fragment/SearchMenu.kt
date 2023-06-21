package br.com.sailboat.todozy.utility.android.fragment

import android.view.Menu

interface SearchMenu {
    fun addSearchMenu(menu: Menu, onSubmitSearch: (String) -> Unit)
}
