package br.com.sailboat.todozy.feature.alarm.impl.presentation.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.alarm.domain.usecase.GetNextAlarmUseCase
import br.com.sailboat.todozy.feature.alarm.impl.domain.service.AlarmManagerService
import br.com.sailboat.todozy.feature.task.details.domain.usecase.DisableTaskUseCase
import br.com.sailboat.todozy.feature.task.details.domain.usecase.GetTaskUseCase
import br.com.sailboat.todozy.feature.task.form.domain.usecase.SaveTaskUseCase
import br.com.sailboat.todozy.feature.task.history.domain.usecase.AddHistoryUseCase
import br.com.sailboat.todozy.utility.android.log.log
import br.com.sailboat.uicomponent.impl.helper.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar

internal class NotificationActionReceiver : BroadcastReceiver(), KoinComponent {
    private val getTaskUseCase: GetTaskUseCase by inject()
    private val getNextAlarmUseCase: GetNextAlarmUseCase by inject()
    private val saveTaskUseCase: SaveTaskUseCase by inject()
    private val disableTaskUseCase: DisableTaskUseCase by inject()
    private val addHistoryUseCase: AddHistoryUseCase by inject()
    private val alarmManagerService: AlarmManagerService by inject()

    override fun onReceive(
        context: Context,
        intent: Intent,
    ) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val taskId = intent.getLongExtra(AlarmReceiver.EXTRA_TASK_ID, -1)
                if (taskId == -1L) return@launch

                when (intent.action) {
                    ACTION_MARK_DONE -> markTaskDone(taskId)
                    ACTION_SNOOZE -> snoozeTask(taskId)
                }

                NotificationHelper().cancelTaskNotification(context, taskId)
                NotificationHelper().cancelSummary(context)
            } catch (e: Exception) {
                e.log()
            } finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun markTaskDone(taskId: Long) {
        val task = getTaskUseCase(taskId).getOrThrow()

        task.alarm?.takeIf { it.isAlarmRepeating() }?.let {
            task.alarm = getNextAlarmUseCase(it)
            saveTaskUseCase(task).getOrThrow()
        } ?: disableTaskUseCase(task).getOrThrow()

        addHistoryUseCase(task, TaskStatus.DONE).getOrThrow()
    }

    private fun snoozeTask(taskId: Long) {
        val calendar =
            Calendar.getInstance().apply {
                add(Calendar.MINUTE, SNOOZE_MINUTES)
            }
        alarmManagerService.scheduleAlarm(calendar, taskId)
    }

    companion object {
        const val ACTION_MARK_DONE = "br.com.sailboat.todozy.action.MARK_DONE"
        const val ACTION_SNOOZE = "br.com.sailboat.todozy.action.SNOOZE"
        private const val SNOOZE_MINUTES = 10
    }
}
