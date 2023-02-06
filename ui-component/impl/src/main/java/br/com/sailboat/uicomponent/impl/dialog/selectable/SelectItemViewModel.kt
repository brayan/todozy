package br.com.sailboat.uicomponent.impl.dialog.selectable

import br.com.sailboat.todozy.utility.android.viewmodel.BaseViewModel

internal class SelectItemViewModel(
    override val viewState: SelectItemViewState = SelectItemViewState(),
) : BaseViewModel<SelectItemViewState, SelectItemViewAction>() {

    override fun dispatchViewIntent(viewIntent: SelectItemViewAction) {
        when (viewIntent) {
            is SelectItemViewAction.OnStart -> onStart(viewIntent)
        }
    }

    private fun onStart(viewAction: SelectItemViewAction.OnStart) {
        viewAction.title?.run { viewState.title.value = this }
        viewAction.selectableItems?.takeIf { it.isNotEmpty() }?.run { viewState.selectableItems.value = this }
        viewAction.selectedItem?.run { viewState.selectedItem.value = this }
    }
}
