package br.com.sailboat.todozy.domain.model

enum class TaskStatus(val id: Int) {

    // TODO: Add TaskStatusData object
    NOT_DONE(0), DONE(1);

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