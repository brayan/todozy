package br.com.sailboat.todozy.ui.helper

import android.content.Intent
import android.os.Bundle
import br.com.sailboat.todozy.domain.helper.EntityHelper

private const val BUNDLE = "BUNDLE"
private const val TASK_ID = "TASK_ID"

fun Intent.putBundle(bundle: Bundle) = putExtra(BUNDLE, bundle)
fun Intent.getBundle(): Bundle? = getBundleExtra(BUNDLE)
fun Bundle.putTaskId(taskId: Long) = putLong(TASK_ID, taskId)
fun Bundle.getTaskId() = getLong(TASK_ID, EntityHelper.NO_ID)
fun Bundle.hasTaskId() = (getLong(TASK_ID, EntityHelper.NO_ID) != EntityHelper.NO_ID)