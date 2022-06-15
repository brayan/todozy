package br.com.sailboat.todozy.utility.android.string

import android.content.Context

class StringProviderImpl(private val context: Context): StringProvider {

    override fun getString(stringRes: Int) = context.getString(stringRes)
}