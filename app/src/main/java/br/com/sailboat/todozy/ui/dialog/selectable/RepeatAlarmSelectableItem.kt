package br.com.sailboat.todozy.ui.dialog.selectable

import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.ui.helper.RepeatTypeView
import java.util.*

enum class RepeatAlarmSelectableItem : SelectableItem {

    NOT_REPEAT {
        override fun getId(): Int {
            return RepeatType.NOT_REPEAT.ordinal
        }

        override fun getName(): Int {
            return RepeatTypeView.getFromRepeatType(RepeatType.NOT_REPEAT)!!.description
        }
    },
    DAY {
        override fun getId(): Int {
            return RepeatType.DAY.ordinal
        }

        override fun getName(): Int {
            return RepeatTypeView.getFromRepeatType(RepeatType.DAY)!!.description
        }
    },
    WEEK {
        override fun getId(): Int {
            return RepeatType.WEEK.ordinal
        }

        override fun getName(): Int {
            return RepeatTypeView.getFromRepeatType(RepeatType.WEEK)!!.description
        }
    },
    MONTH {
        override fun getId(): Int {
            return RepeatType.MONTH.ordinal
        }

        override fun getName(): Int {
            return RepeatTypeView.getFromRepeatType(RepeatType.MONTH)!!.description
        }
    },
    YEAR {
        override fun getId(): Int {
            return RepeatType.YEAR.ordinal
        }

        override fun getName(): Int {
            return RepeatTypeView.getFromRepeatType(RepeatType.YEAR)!!.description
        }
    },
    CUSTOM {
        override fun getId(): Int {
            return RepeatType.CUSTOM.ordinal
        }

        override fun getName(): Int {
            return RepeatTypeView.getFromRepeatType(RepeatType.CUSTOM)!!.description
        }
    };

    companion object {
        fun getFromId(id: Int): RepeatAlarmSelectableItem {
            val repeatType = RepeatType.indexOf(id)

            when (repeatType) {
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