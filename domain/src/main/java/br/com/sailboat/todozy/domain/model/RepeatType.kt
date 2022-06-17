package br.com.sailboat.todozy.domain.model

enum class RepeatType {

    NOT_REPEAT, DAY, WEEK, MONTH, YEAR, SECOND, MINUTE, HOUR, CUSTOM;

    companion object {
        fun isAlarmRepeating(alarm: Alarm): Boolean {
            return alarm.repeatType != NOT_REPEAT
        }

        fun indexOf(index: Int): RepeatType {
            for (repeatType in values()) {
                if (repeatType.ordinal == index) {
                    return repeatType
                }
            }
            return NOT_REPEAT
        }
    }
}
