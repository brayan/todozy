package br.com.sailboat.todozy.platform.impl

import android.content.Context
import br.com.sailboat.todozy.utility.kotlin.StringProvider

internal class StringProviderImpl(private val context: Context) : StringProvider {
    override fun getString(stringRes: Int) = context.getString(stringRes)
}
