package br.com.sailboat.uicomponent.impl.viewholder.factory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import br.com.sailboat.uicomponent.impl.databinding.AlarmDetailsBinding
import br.com.sailboat.uicomponent.impl.viewholder.AlarmViewHolder2
import br.com.sailboat.uicomponent.impl.viewholder.EmptyViewHolder
import br.com.sailboat.uicomponent.model.UiModelType

class SDUIActionHandler {
    fun notifyEvent() {
    }
}

class ViewHolderFactoryProvider {

    private val hashSetTest = hashMapOf(
        UiModelType.ALARM.ordinal to AlarmViewHolderFactory()
    )

    fun create(uiModelId: Int, parent: ViewGroup): RecyclerView.ViewHolder {
        val viewHolderFactory = hashSetTest[uiModelId]
        return viewHolderFactory?.create(parent) ?: EmptyViewHolder(parent).also { logViewHolderNotFound(uiModelId) }
    }

    private fun logViewHolderNotFound(uiModelId: Int) {
        // TODO: Log
    }
}

abstract class ViewHolderFactory<VB : ViewBinding>(private val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB) {
    protected val actionHandler: SDUIActionHandler by lazy { SDUIActionHandler() }

    abstract fun create(parent: ViewGroup): RecyclerView.ViewHolder

    protected fun inflate(parent: ViewGroup): VB {
        val inflater = LayoutInflater.from(parent.context)
        return bindingInflater(inflater, parent, false)
    }
}

class AlarmViewHolderFactory : ViewHolderFactory<AlarmDetailsBinding>(AlarmDetailsBinding::inflate) {
    override fun create(parent: ViewGroup) = AlarmViewHolder2(inflate(parent))
}
