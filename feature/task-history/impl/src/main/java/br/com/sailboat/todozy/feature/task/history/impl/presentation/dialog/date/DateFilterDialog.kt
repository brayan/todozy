package br.com.sailboat.todozy.feature.task.history.impl.presentation.dialog.date

import androidx.fragment.app.FragmentManager
import br.com.sailboat.uicomponent.impl.dialog.selectable.SelectItemDialog
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.SelectableItem

object DateFilterDialog {

    val TAG: String = DateFilterDialog::class.java.name

    fun show(
        manager: FragmentManager,
        title: String,
        items: List<SelectableItem>,
        selectedItem: SelectableItem?,
        callback: SelectItemDialog.Callback,
    ) = SelectItemDialog.show(
        TAG,
        manager,
        title,
        items,
        selectedItem,
        callback,
    )
}
