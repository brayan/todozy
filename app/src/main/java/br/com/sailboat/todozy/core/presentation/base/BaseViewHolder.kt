package br.com.sailboat.todozy.core.presentation.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<in T, VB : ViewBinding>(val binding: VB) :
    RecyclerView.ViewHolder(binding.root) {

    val context: Context by lazy { itemView.context }

    abstract fun bind(item: T)

    companion object {
        fun getInflater(parent: ViewGroup): LayoutInflater = LayoutInflater.from(parent.context)
    }

}