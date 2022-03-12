package br.com.sailboat.todozy.feature.about.impl.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.uicomponent.helper.UiModelDiffUtilCallback
import br.com.sailboat.todozy.uicomponent.model.ImageTitleDividerUiModel
import br.com.sailboat.todozy.uicomponent.model.LabelValueUiModel
import br.com.sailboat.todozy.uicomponent.model.UiModel
import br.com.sailboat.todozy.uicomponent.model.UiModelType
import br.com.sailboat.todozy.uicomponent.viewholder.EmptyViewHolder
import br.com.sailboat.todozy.uicomponent.viewholder.ImageTitleDividerViewHolder
import br.com.sailboat.todozy.uicomponent.viewholder.LabelValueViewHolder

class AboutAdapter :
    ListAdapter<UiModel, RecyclerView.ViewHolder>(UiModelDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        UiModelType.IMAGE_TITLE.ordinal -> ImageTitleDividerViewHolder(parent)
        UiModelType.LABEL_VALUE.ordinal -> LabelValueViewHolder(parent)
        else -> EmptyViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (item.uiModelId) {
            UiModelType.IMAGE_TITLE.ordinal -> (holder as ImageTitleDividerViewHolder).bind(item as ImageTitleDividerUiModel)
            UiModelType.LABEL_VALUE.ordinal -> (holder as LabelValueViewHolder).bind(item as LabelValueUiModel)
        }
    }

    override fun getItemViewType(position: Int) = getItem(position).uiModelId

}