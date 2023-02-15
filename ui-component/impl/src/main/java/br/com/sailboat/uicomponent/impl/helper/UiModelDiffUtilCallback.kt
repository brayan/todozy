package br.com.sailboat.uicomponent.impl.helper

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import br.com.sailboat.uicomponent.model.UiModel

class UiModelDiffUtilCallback : DiffUtil.ItemCallback<UiModel>() {
    override fun areItemsTheSame(
        oldItem: UiModel,
        newItem: UiModel,
    ) = oldItem == newItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: UiModel,
        newItem: UiModel,
    ) = oldItem == newItem
}
