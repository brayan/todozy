package br.com.sailboat.todozy.core.platform

import android.content.Context
import br.com.sailboat.todozy.utility.android.string.StringProvider

class StringProviderImpl(private val context: Context): StringProvider {

    override fun getString(stringRes: Int) = context.getString(stringRes)
}
