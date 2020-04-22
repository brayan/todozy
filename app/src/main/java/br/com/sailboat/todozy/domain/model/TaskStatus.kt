package br.com.sailboat.todozy.domain.model

enum class TaskStatus(val id: Int) {

    DONE(0), NOT_DONE(1);

    companion object {

        fun getById(id: Int): TaskStatus {
            for (value in values()) {
                if (value.id == id) {
                    return value
                }
            }

            throw IndexOutOfBoundsException()
        }

    }
}