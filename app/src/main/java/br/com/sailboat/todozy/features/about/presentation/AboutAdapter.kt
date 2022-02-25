package br.com.sailboat.todozy.features.about.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.core.presentation.viewholder.ImageTitleDividerViewHolder
import br.com.sailboat.todozy.core.presentation.viewholder.LabelValueViewHolder
import br.com.sailboat.todozy.uicomponent.model.ImageTitleDividerUiModel
import br.com.sailboat.todozy.uicomponent.model.LabelValueUiModel
import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.uicomponent.model.ViewType

class AboutAdapter(private val callback: Callback) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface Callback {
        fun getAbout(): List<UiModel>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        ViewType.IMAGE_TITLE.ordinal -> ImageTitleDividerViewHolder(parent)
        ViewType.LABEL_VALUE.ordinal -> LabelValueViewHolder(parent)
        else -> throw RuntimeException("ViewHolder not found")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = callback.getAbout()[position]

        when (item.index) {
            ViewType.IMAGE_TITLE.ordinal -> (holder as ImageTitleDividerViewHolder).bind(item as ImageTitleDividerUiModel)
            ViewType.LABEL_VALUE.ordinal -> (holder as LabelValueViewHolder).bind(item as LabelValueUiModel)
            else -> throw RuntimeException("ViewHolder not found")
        }
    }

    override fun getItemCount() = callback.getAbout().size

    override fun getItemViewType(position: Int) = callback.getAbout()[position].index

}