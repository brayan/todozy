package br.com.sailboat.todozy.uicomponent.helper

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import br.com.sailboat.todozy.uicomponent.model.UiModel

class UiModelDiffUtilCallback : DiffUtil.ItemCallback<UiModel>() {
    override fun areItemsTheSame(
        oldItem: UiModel,
        newItem: UiModel,
    ) = oldItem.uiModelId == newItem.uiModelId

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: UiModel,
        newItem: UiModel,
    ) = oldItem == newItem
}