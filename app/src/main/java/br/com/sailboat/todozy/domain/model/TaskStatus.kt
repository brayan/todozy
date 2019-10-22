package br.com.sailboat.todozy.domain.model

enum class TaskStatus {
    DONE, NOT_DONE;

    companion object {

        fun indexOf(index: Int): TaskStatus {
            for (value in values()) {
                if (value.ordinal == index) {
                    return value
                }
            }

            throw IndexOutOfBoundsException()
        }

    }
}