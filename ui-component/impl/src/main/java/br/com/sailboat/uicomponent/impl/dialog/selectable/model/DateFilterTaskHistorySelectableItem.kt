package br.com.sailboat.uicomponent.impl.dialog.selectable.model

import br.com.sailboat.uicomponent.impl.R
import java.util.Arrays

enum class DateFilterTaskHistorySelectableItem : SelectableItem {

    NO_FILTER {
        override fun getName(): Int {
            return R.string.no_filter
        }
    },
    TODAY {
        override fun getName(): Int {
            return R.string.today
        }
    },
    YESTERDAY {
        override fun getName(): Int {
            return R.string.yesterday
        }
    },
    LAST_7_DAYS {
        override fun getName(): Int {
            return R.string.last_7_days
        }
    },
    LAST_30_DAYS {
        override fun getName(): Int {
            return R.string.last_30_days
        }
    },
    DATE_RANGE {
        override fun getName(): Int {
            return R.string.date_range
        }
    };

    companion object {
        fun getItems(): List<SelectableItem> {
            return ArrayList<SelectableItem>(Arrays.asList(*values()))
        }
    }
}
