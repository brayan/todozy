package br.com.sailboat.uicomponent.impl.dialog.selectable

import br.com.sailboat.uicomponent.impl.dialog.selectable.model.SelectableItem

internal sealed class SelectItemViewAction {
    data class OnStart(
        val title: String?,
        val selectableItems: List<SelectableItem>?,
        val selectedItem: SelectableItem?,
    ) : SelectItemViewAction()
}
