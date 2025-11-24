package br.com.sailboat.todozy.feature.about.impl.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.uicomponent.impl.helper.UiModelDiffUtilCallback
import br.com.sailboat.uicomponent.impl.viewholder.EmptyViewHolder
import br.com.sailboat.uicomponent.impl.viewholder.ImageTitleDividerViewHolder
import br.com.sailboat.uicomponent.impl.viewholder.LabelValueViewHolder
import br.com.sailboat.uicomponent.model.ImageTitleDividerUiModel
import br.com.sailboat.uicomponent.model.LabelValueUiModel
import br.com.sailboat.uicomponent.model.UiModel
import br.com.sailboat.uicomponent.model.UiModelType

internal class AboutAdapter :
    ListAdapter<UiModel, RecyclerView.ViewHolder>(UiModelDiffUtilCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ) = when (viewType) {
        UiModelType.IMAGE_TITLE.ordinal -> ImageTitleDividerViewHolder(parent)
        UiModelType.LABEL_VALUE.ordinal -> LabelValueViewHolder(parent)
        else -> EmptyViewHolder(parent)
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is ImageTitleDividerUiModel -> (holder as ImageTitleDividerViewHolder).bind(item)
            is LabelValueUiModel -> (holder as LabelValueViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).uiModelId
}
