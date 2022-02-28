package br.com.sailboat.todozy.uicomponent.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.todozy.uicomponent.databinding.VhEmptyBinding

class EmptyViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    VhEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
)
