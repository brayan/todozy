package br.com.sailboat.uicomponent.impl.dialog.selectable.model

import br.com.sailboat.uicomponent.impl.R
import java.util.*

enum class TaskStatusSelectableItem : SelectableItem {

    NO_FILTER {
        override fun getName(): Int {
            return R.string.no_filter
        }
    },
    TASKS_DONE {
        override fun getName(): Int {
            return R.string.tasks_done
        }
    },
    TASKS_NOT_DONE {
        override fun getName(): Int {
            return R.string.tasks_not_done
        }
    };

    companion object {
        fun getItems(): List<SelectableItem> {
            return ArrayList<SelectableItem>(Arrays.asList(*values()))
        }
    }

}