package br.com.sailboat.uicomponent.impl.dialog.selectable

import androidx.lifecycle.MutableLiveData
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.SelectableItem

internal class SelectItemViewState {
    val title = MutableLiveData<String>()
    val selectableItems = MutableLiveData<List<SelectableItem>>()
    val selectedItem = MutableLiveData<SelectableItem>()
}
