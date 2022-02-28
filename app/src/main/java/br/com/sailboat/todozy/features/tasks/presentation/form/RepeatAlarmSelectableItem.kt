package br.com.sailboat.todozy.features.tasks.presentation.form

import br.com.sailboat.todozy.R
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.uicomponent.dialog.selectable.model.SelectableItem
import java.util.*

enum class RepeatAlarmSelectableItem : SelectableItem {

    NOT_REPEAT {
        override fun getId(): Int {
            return ordinal
        }

        override fun getName(): Int {
            return R.string.not_repeat
        }
    },
    DAY {
        override fun getId(): Int {
            return ordinal
        }

        override fun getName(): Int {
            return R.string.every_day
        }
    },
    WEEK {
        override fun getId(): Int {
            return ordinal
        }

        override fun getName(): Int {
            return R.string.every_week
        }
    },
    MONTH {
        override fun getId() = ordinal

        override fun getName(): Int {
            return R.string.every_month
        }
    },
    YEAR {
        override fun getId() = ordinal

        override fun getName(): Int {
            return R.string.every_year
        }
    },
    CUSTOM {
        override fun getId() = ordinal

        override fun getName(): Int {
            return R.string.custom
        }
    };

    companion object {
        fun getFromId(id: Int): RepeatAlarmSelectableItem {

            when (RepeatType.indexOf(id)) {
                RepeatType.DAY -> {
                    return DAY
                }
                RepeatType.WEEK -> {
                    return WEEK
                }
                RepeatType.MONTH -> {
                    return MONTH
                }
                RepeatType.YEAR -> {
                    return YEAR
                }
                RepeatType.CUSTOM -> {
                    return CUSTOM
                }
                else -> {
                    return NOT_REPEAT
                }
            }
        }

        fun getItems(): List<SelectableItem> {
            return ArrayList<SelectableItem>(Arrays.asList(*values()))
        }
    }

    abstract fun getId(): Int

}