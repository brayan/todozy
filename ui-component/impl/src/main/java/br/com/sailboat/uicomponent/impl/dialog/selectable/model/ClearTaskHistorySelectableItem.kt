package br.com.sailboat.uicomponent.impl.dialog.selectable.model

import br.com.sailboat.uicomponent.impl.R

enum class ClearTaskHistorySelectableItem : SelectableItem {

    CLEAR_HISTORY_KEEP_AMOUNT {
        override fun getName(): Int {
            return R.string.clear_history_but_keep
        }
    },
    CLEAR_ALL_HISTORY {
        override fun getName(): Int {
            return R.string.clear_all_history
        }
    };

    companion object {
        fun getItems(): List<SelectableItem> {
            return ArrayList<SelectableItem>(listOf(*values()))
        }
    }
}
