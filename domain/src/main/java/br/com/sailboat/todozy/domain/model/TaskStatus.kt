package br.com.sailboat.todozy.domain.model

enum class TaskStatus(val id: Int) {
    NOT_DONE(0),
    DONE(1),
    ;

    companion object {
        fun getById(id: Int): TaskStatus = TaskStatus.entries.firstOrNull { it.id == id } ?: NOT_DONE
    }
}
