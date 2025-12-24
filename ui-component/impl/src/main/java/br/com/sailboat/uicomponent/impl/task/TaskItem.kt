package br.com.sailboat.uicomponent.impl.task

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.utility.android.calendar.formatTimeWithAndroidFormat
import br.com.sailboat.todozy.utility.android.calendar.getMonthAndDayShort
import br.com.sailboat.todozy.utility.android.calendar.toShortDateView
import br.com.sailboat.todozy.utility.kotlin.extension.isAfterTomorrow
import br.com.sailboat.todozy.utility.kotlin.extension.isBeforeToday
import br.com.sailboat.todozy.utility.kotlin.extension.isCurrentYear
import br.com.sailboat.uicomponent.impl.R
import br.com.sailboat.uicomponent.impl.theme.LocalTodozySpacing
import br.com.sailboat.uicomponent.impl.theme.TodozyTheme
import br.com.sailboat.uicomponent.model.TaskUiModel
import java.util.Calendar

private val TASK_ROW_MIN_HEIGHT = 64.dp

@Composable
@Suppress("FunctionName")
@OptIn(ExperimentalFoundationApi::class)
fun TaskItem(
    task: TaskUiModel,
    modifier: Modifier = Modifier,
    onClick: (Long) -> Unit,
    onLongClick: (Long) -> Unit = {},
    onUndoClick: (Long, TaskStatus) -> Unit,
) {
    val context = LocalContext.current
    val spacing = LocalTodozySpacing.current
    val inlineElevation = 6.dp
    val interactionSource = remember { MutableInteractionSource() }
    val contentPadding =
        PaddingValues(
            start = spacing.medium,
            end = spacing.medium,
            top = spacing.small,
            bottom = spacing.medium,
        )
    val alarmInfo = remember(task.alarm, context) {
        resolveAlarmInfo(context, task.alarm)
    }
    val alarmColor = task.alarmColor?.let { Color(it) } ?: MaterialTheme.colors.primary

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .animateContentSize()
                .combinedClickable(
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    onClick = { onClick(task.taskId) },
                    onLongClick = { onLongClick(task.taskId) },
                ),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = if (task.showInlineMetrics) inlineElevation else 0.dp,
        shape = MaterialTheme.shapes.medium.copy(all = ZeroCornerSize),
    ) {
        AnimatedContent(
            targetState = task.showInlineMetrics,
            transitionSpec = {
                (fadeIn(tween(150)) + expandVertically()) togetherWith
                    (fadeOut(tween(150)) + shrinkVertically())
            },
            label = "task-inline-metrics",
        ) { showInline ->
            if (showInline) {
                InlineMetrics(
                    metrics = task.inlineMetrics,
                    status = task.inlineStatus ?: TaskStatus.NOT_DONE,
                    onUndoClick = { status -> onUndoClick(task.taskId, status) },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .heightIn(min = TASK_ROW_MIN_HEIGHT)
                            .background(MaterialTheme.colors.primary, shape = MaterialTheme.shapes.medium.copy(all = ZeroCornerSize))
                            .padding(contentPadding),
                    spacingSmall = spacing.small,
                    spacingXSmall = spacing.xsmall,
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = TASK_ROW_MIN_HEIGHT)
                        .padding(contentPadding),
                    verticalAlignment = Alignment.Top,
                ) {
                    Text(
                        text = task.taskName,
                        color = colorResource(id = R.color.md_blue_grey_700),
                        style =
                            MaterialTheme.typography.body1.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                            ),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )

                    if (alarmInfo != null) {
                        Spacer(modifier = Modifier.width(spacing.small))
                        TaskAlarm(
                            alarmInfo = alarmInfo,
                            color = alarmColor,
                            spacingXSmall = spacing.xsmall,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskAlarm(
    alarmInfo: AlarmInfo,
    color: Color,
    spacingXSmall: Dp,
) {
    when (alarmInfo.type) {
        AlarmType.TIME ->
            Text(
                text = alarmInfo.text,
                color = color,
                style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Light),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

        AlarmType.DATE ->
            Text(
                text = alarmInfo.text,
                color = color,
                style = MaterialTheme.typography.caption.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = spacingXSmall),
            )
    }
}

@Composable
private fun InlineMetrics(
    metrics: TaskMetrics?,
    status: TaskStatus,
    onUndoClick: (TaskStatus) -> Unit,
    modifier: Modifier = Modifier,
    spacingSmall: Dp,
    spacingXSmall: Dp,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacingSmall * 1.5f),
    ) {
        MetricsContent(
            metrics = metrics,
            modifier = Modifier.weight(1f),
            spacingSmall = spacingSmall,
            spacingXSmall = spacingXSmall,
        )

        TextButton(
            onClick = { onUndoClick(status) },
            colors =
                ButtonDefaults.textButtonColors(
                    contentColor = Color.White,
                ),
            contentPadding =
                PaddingValues(
                    horizontal = spacingSmall,
                    vertical = spacingXSmall,
                ),
        ) {
            Text(
                text = stringResource(id = R.string.undo),
                color = Color.White,
                style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Bold),
            )
        }
    }
}

@Composable
private fun MetricsContent(
    metrics: TaskMetrics?,
    modifier: Modifier = Modifier,
    spacingSmall: Dp,
    spacingXSmall: Dp,
) {
    val metricChips =
        listOfNotNull(
            metrics?.consecutiveDone?.takeIf { it > 0 }?.let {
                MetricData(
                    icon = R.drawable.ic_fire_black_24dp,
                    tint = colorResource(id = R.color.md_orange_500),
                    label = it.toString(),
                )
            },
            metrics?.doneTasks?.let {
                MetricData(
                    icon = R.drawable.ic_vec_thumb_up_white_24dp,
                    tint = colorResource(id = R.color.md_teal_300),
                    label = it.toString(),
                )
            },
            metrics?.notDoneTasks?.let {
                MetricData(
                    icon = R.drawable.ic_vect_thumb_down_white_24dp,
                    tint = colorResource(id = R.color.md_red_300),
                    label = it.toString(),
                )
            },
        ).filter { it.label.isNotEmpty() }

    if (metricChips.isEmpty()) return

    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        metricChips.forEach { chip ->
            MetricChip(
                icon = chip.icon,
                iconTint = chip.tint,
                label = chip.label,
                spacingXSmall = spacingXSmall,
            )
        }
    }
}

@Composable
private fun MetricChip(
    icon: Int,
    iconTint: Color,
    label: String,
    spacingXSmall: Dp,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacingXSmall),
    ) {
        Surface(
            shape = CircleShape,
            color = Color.White,
            modifier = Modifier.size(28.dp),
            elevation = 0.dp,
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.padding(spacingXSmall),
            )
        }

        Text(
            text = label,
            color = Color.White,
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Bold),
        )
    }
}

private data class MetricData(
    val icon: Int,
    val tint: Color,
    val label: String,
)

private fun resolveAlarmInfo(
    context: Context,
    alarm: Calendar?,
): AlarmInfo? {
    alarm ?: return null

    return if (alarm.isBeforeToday() || alarm.isAfterTomorrow()) {
        val dateText =
            if (alarm.isCurrentYear()) {
                alarm.getMonthAndDayShort(context)
            } else {
                alarm.toShortDateView(context)
            }

        AlarmInfo(text = dateText, type = AlarmType.DATE)
    } else {
        AlarmInfo(text = alarm.formatTimeWithAndroidFormat(context), type = AlarmType.TIME)
    }
}

private data class AlarmInfo(
    val text: String,
    val type: AlarmType,
)

private enum class AlarmType { DATE, TIME }

@Preview(showBackground = true)
@Composable
private fun TaskItemPreview() {
    val alarm =
        remember {
            Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 15)
                set(Calendar.MINUTE, 0)
            }
        }
    MaterialTheme {
        TodozyTheme {
            TaskItem(
                task =
                    TaskUiModel(
                        taskId = 1,
                        taskName = "Review pull requests",
                        alarm = alarm,
                        alarmColor = Color(0xFF0097A7).toArgb(),
                    ),
                onClick = {},
                onUndoClick = { _, _ -> },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TaskItemInlinePreview() {
    MaterialTheme {
        TodozyTheme {
            TaskItem(
                task =
                    TaskUiModel(
                        taskId = 2,
                        taskName = "Write daily summary",
                        showInlineMetrics = true,
                        inlineMetrics =
                            TaskMetrics(
                                doneTasks = 12,
                                notDoneTasks = 3,
                                consecutiveDone = 5,
                            ),
                        inlineStatus = TaskStatus.DONE,
                    ),
                onClick = {},
                onUndoClick = { _, _ -> },
            )
        }
    }
}
