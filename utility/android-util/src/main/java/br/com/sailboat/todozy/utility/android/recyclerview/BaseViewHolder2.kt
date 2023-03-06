package br.com.sailboat.todozy.utility.android.recyclerview

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder2<VB : ViewBinding, in UiModel>(val view: View) : RecyclerView.ViewHolder(view) {

    val context: Context by lazy { itemView.context }

    abstract fun VB.bind(item: UiModel)
}
