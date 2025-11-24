package br.com.sailboat.todozy.feature.task.form.impl.presentation

import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.uicomponent.impl.dialog.selectable.model.SelectableItem
import java.util.Arrays
import br.com.sailboat.uicomponent.impl.R as UiR

internal enum class RepeatAlarmSelectableItem : SelectableItem {
    NOT_REPEAT {
        override fun getId(): Int {
            return ordinal
        }

        override fun getName(): Int {
            return UiR.string.not_repeat
        }
    },
    DAY {
        override fun getId(): Int {
            return ordinal
        }

        override fun getName(): Int {
            return UiR.string.every_day
        }
    },
    WEEK {
        override fun getId(): Int {
            return ordinal
        }

        override fun getName(): Int {
            return UiR.string.every_week
        }
    },
    MONTH {
        override fun getId() = ordinal

        override fun getName(): Int {
            return UiR.string.every_month
        }
    },
    YEAR {
        override fun getId() = ordinal

        override fun getName(): Int {
            return UiR.string.every_year
        }
    },
    CUSTOM {
        override fun getId() = ordinal

        override fun getName(): Int {
            return UiR.string.custom
        }
    }, ;

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
