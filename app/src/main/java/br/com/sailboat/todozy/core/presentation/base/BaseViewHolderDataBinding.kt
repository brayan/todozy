package br.com.sailboat.todozy.core.presentation.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolderDataBinding<in T>(val viewDataBiding: ViewDataBinding) : RecyclerView.ViewHolder(viewDataBiding.root) {

    val context: Context by lazy { itemView.context }

    abstract fun bind(item: T)

    companion object {
        fun inflate(parent: ViewGroup, layoutId: Int): ViewDataBinding {
            val inflater = LayoutInflater.from(parent.context)
            return DataBindingUtil.inflate(inflater, layoutId, parent, false)
        }
    }

}