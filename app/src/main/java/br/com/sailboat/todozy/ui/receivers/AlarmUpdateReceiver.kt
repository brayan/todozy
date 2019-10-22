package br.com.sailboat.todozy.ui.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmUpdateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.run { initAlarms(context) }
    }

    private fun initAlarms(context: Context) {

//        try {
//            val tasks = TaskRepositoryImpl(context).getTasksWithAlarms()
//
//            for (task in tasks) {
//                val alarm = AlarmSQLite.newInstance(context).getAlarmByTask(task.id)
//                AlarmManagerHelper(context).setNextValidAlarm(context, task, alarm)
//            }
//
//        } catch (e: Exception) {
//            LogHelper.logException(e)
//        }

    }


}