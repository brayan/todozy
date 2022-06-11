package br.com.sailboat.uicomponent.impl.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.sailboat.uicomponent.impl.databinding.VhEmptyBinding

class EmptyViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    VhEmptyBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
)
