package br.com.sailboat.todozy.ui.about

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.ui.model.*
import br.com.sailboat.todozy.ui.model.view_holder.ImageTitleDividerViewHolder
import br.com.sailboat.todozy.ui.model.view_holder.LabelValueViewHolder

class AboutAdapter (private val callback: Callback) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Callback {
        fun getAbout(): List<ItemView>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ViewType.IMAGE_TITLE.ordinal -> ImageTitleDividerViewHolder(parent)
        ViewType.LABEL_VALUE.ordinal -> LabelValueViewHolder(parent)
        else -> throw RuntimeException("ViewHolder not found")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = callback.getAbout()[position]

        when (item.viewType) {
            ViewType.IMAGE_TITLE.ordinal -> (holder as ImageTitleDividerViewHolder).bind(item as ImageTitleDividerItemView)
            ViewType.LABEL_VALUE.ordinal -> (holder as LabelValueViewHolder).bind(item as LabelValueItemView)
            else -> throw RuntimeException("ViewHolder not found")
        }
    }

    override fun getItemCount() = callback.getAbout().size

    override fun getItemViewType(position: Int) = callback.getAbout()[position].viewType

}