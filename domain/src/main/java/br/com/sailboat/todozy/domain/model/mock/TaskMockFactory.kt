package br.com.sailboat.todozy.domain.model.mock

import br.com.sailboat.todozy.domain.model.Alarm
import br.com.sailboat.todozy.domain.model.RepeatType
import br.com.sailboat.todozy.domain.model.Task
import java.util.*

object TaskMockFactory {

    fun makeTask(
        id: Long = 45L,
        name: String = "Task Name",
        notes: String? = "Task Notes",
        alarm: Alarm? = Alarm(
            dateTime = Calendar.getInstance(),
            repeatType = RepeatType.WEEK,
        )
    ): Task {
        return Task(
            id = id,
            name = name,
            notes = notes,
            alarm = alarm,
        )
    }

    fun makeTaskList(task: Task = makeTask()) = listOf(task)
}