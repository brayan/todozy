package br.com.sailboat.todozy.ui.dialog.selectable

import br.com.sailboat.todozy.R
import java.util.*

enum class ClearTaskHistorySelectableItem: SelectableItem {

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
            return ArrayList<SelectableItem>(Arrays.asList(*values()))
        }
    }


}