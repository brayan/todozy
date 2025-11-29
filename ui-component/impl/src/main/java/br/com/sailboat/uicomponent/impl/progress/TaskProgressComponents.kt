@file:Suppress("FunctionName")

package br.com.sailboat.uicomponent.impl.progress

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import br.com.sailboat.todozy.domain.model.TaskProgressDay
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.uicomponent.impl.R
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

internal val DefaultTaskProgressDayOrder =
    listOf(
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY,
    )

@Composable
internal fun TaskProgressContent(
    days: List<TaskProgressDay>,
    selectedRange: TaskProgressRange,
    onRangeSelected: (TaskProgressRange) -> Unit,
    onDayClick: ((TaskProgressDay) -> Unit)?,
    isLoading: Boolean = false,
    enableDayDetails: Boolean = false,
    visibleDaysOfWeek: List<DayOfWeek> = DefaultTaskProgressDayOrder,
    highlightNotDone: Boolean = false,
    flatColors: Boolean = highlightNotDone,
) {
    val palette = rememberProgressPalette(flatColors = flatColors || highlightNotDone)
    val totalDone = remember(days) { days.sumOf { it.doneCount } }
    val formatter = remember { DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM) }
    val (selectedDay, onSelectDay) = remember { mutableStateOf<TaskProgressDay?>(null) }
    val haptic = LocalHapticFeedback.current
    val dayOrder = visibleDaysOfWeek.ifEmpty { DefaultTaskProgressDayOrder }
    val cellSize = remember(dayOrder.size) { if (dayOrder.size <= 2) 24.dp else 16.dp }

    Column(
        modifier =
            Modifier
                .fillMaxWidth(),
    ) {
        TaskProgressRangeSelector(
            selectedRange = selectedRange,
            onRangeSelected = onRangeSelected,
        )
        Spacer(modifier = Modifier.size(8.dp))
        if (isLoading) {
            TaskProgressSkeleton(dayOrder = dayOrder, cellSize = cellSize)
        } else {
            val dayClickHandler: ((TaskProgressDay) -> Unit)? =
                when {
                    enableDayDetails && onDayClick != null -> {
                        { day ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onDayClick(day)
                            onSelectDay(if (selectedDay == day) null else day)
                        }
                    }
                    enableDayDetails -> { day ->
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSelectDay(if (selectedDay == day) null else day)
                    }
                    else -> onDayClick
                }

            TaskProgressGrid(
                days = days,
                palette = palette,
                dayOrder = dayOrder,
                cellSize = cellSize,
                onDayClick = dayClickHandler,
                onDayHaptic = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) },
                selectedDay = selectedDay,
                onDismissTooltip = { onSelectDay(null) },
                formatter = formatter,
                enableDayDetails = enableDayDetails,
                highlightNotDone = highlightNotDone,
            )
        }
    }
}

@Composable
private fun TaskProgressRangeSelector(
    selectedRange: TaskProgressRange,
    onRangeSelected: (TaskProgressRange) -> Unit,
) {
    val haptic = LocalHapticFeedback.current
    val ranges =
        remember {
            listOf(
                TaskProgressRange.ALL,
                TaskProgressRange.LAST_YEAR,
                TaskProgressRange.LAST_30_DAYS,
                TaskProgressRange.LAST_7_DAYS,
            )
        }

    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(ranges) { range ->
            val selected = range == selectedRange
            val shape = RoundedCornerShape(12.dp)
            Surface(
                modifier =
                    Modifier
                        .clip(shape)
                        .clickable {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onRangeSelected(range)
                        },
                color =
                    if (selected) {
                        colorResource(id = R.color.md_teal_100)
                    } else {
                        MaterialTheme.colors.surface
                    },
                shape = shape,
                border =
                    BorderStroke(
                        width = 1.dp,
                        color =
                            if (selected) {
                                colorResource(id = R.color.md_teal_500)
                            } else {
                                MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                            },
                    ),
            ) {
                Text(
                    text = range.toLabel(),
                    style = MaterialTheme.typography.caption,
                    color =
                        if (selected) {
                            colorResource(id = R.color.md_teal_700)
                        } else {
                            MaterialTheme.colors.onSurface
                        },
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun TaskProgressGrid(
    days: List<TaskProgressDay>,
    palette: ProgressPalette,
    dayOrder: List<DayOfWeek>,
    cellSize: Dp,
    onDayClick: ((TaskProgressDay) -> Unit)?,
    onDayHaptic: (() -> Unit)?,
    selectedDay: TaskProgressDay?,
    onDismissTooltip: () -> Unit,
    formatter: DateTimeFormatter,
    enableDayDetails: Boolean,
    highlightNotDone: Boolean,
) {
    if (days.isEmpty()) {
        Text(
            text = stringResource(id = R.string.no_history_found),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 4.dp),
        )
        return
    }

    val locale = Locale.getDefault()
    val dayMetadata =
        remember(days, palette, locale) {
            days.associateBy(
                keySelector = { it.date },
                valueTransform = { day ->
                    val label = day.date.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
                    val description =
                        buildString {
                            append("$label ${day.date} (${day.doneCount}/${day.totalCount})")
                            if (day.notDoneCount > 0) {
                                append(" - ")
                                append("${day.notDoneCount} not done")
                            }
                        }
                    DayHeatmapMetadata(
                        color = day.color(palette, highlightNotDone),
                        semanticsDescription = description,
                    )
                },
            )
        }

    val paddedDays = remember(days, dayOrder) { padDays(days, dayOrder) }
    val weeks = remember(paddedDays, dayOrder.size) { paddedDays.chunked(dayOrder.size) }
    val lastWeekIndex = remember(weeks.size) { weeks.lastIndex.coerceAtLeast(0) }
    val lazyListState: LazyListState =
        rememberSaveable(weeks.size, saver = LazyListState.Saver) {
            LazyListState(lastWeekIndex)
        }

    Row(verticalAlignment = Alignment.Top) {
        WeekdayLabels(dayOrder = dayOrder, cellSize = cellSize)

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            state = lazyListState,
        ) {
            items(weeks) { week ->
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    week.forEach { day ->
                        val metadata = day?.let { dayMetadata[it.date] }
                        val color = metadata?.color ?: palette.neutral
                        val semanticsDescription = metadata?.semanticsDescription

                        Box(
                            modifier =
                                Modifier
                                    .size(cellSize)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(color)
                                    .let {
                                        if (day != null && onDayClick != null) {
                                            it.clickable {
                                                onDayHaptic?.invoke()
                                                onDayClick(day)
                                            }
                                        } else {
                                            it
                                        }
                                    }
                                    .semantics {
                                        semanticsDescription?.let { desc -> contentDescription = desc }
                                    },
                        ) {
                            if (enableDayDetails && day != null && selectedDay == day) {
                                DayTooltip(
                                    day = day,
                                    formatter = formatter,
                                    onDismiss = onDismissTooltip,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskProgressSkeleton(
    dayOrder: List<DayOfWeek> = DefaultTaskProgressDayOrder,
    cellSize: Dp,
) {
    val placeholderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.08f)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth(0.3f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(placeholderColor),
        )

        Row(verticalAlignment = Alignment.Top) {
            WeekdayLabels(dayOrder = dayOrder, cellSize = cellSize)

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(5) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        repeat(dayOrder.size) {
                            Box(
                                modifier =
                                    Modifier
                                        .size(cellSize)
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(placeholderColor),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DayTooltip(
    day: TaskProgressDay,
    formatter: DateTimeFormatter,
    onDismiss: () -> Unit,
) {
    val summary = stringResource(R.string.tooltip_tasks_summary, day.doneCount, day.totalCount)

    DropdownMenu(
        expanded = true,
        onDismissRequest = onDismiss,
        offset = DpOffset(0.dp, (-8).dp),
    ) {
        DropdownMenuItem(onClick = onDismiss) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = formatter.format(day.date),
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = summary,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface,
                )
            }
        }
    }
}

@Composable
private fun WeekdayLabels(
    dayOrder: List<DayOfWeek>,
    cellSize: Dp,
) {
    val labeledDays =
        remember(dayOrder) {
            if (dayOrder.size < 7) {
                dayOrder.toSet()
            } else {
                setOf(
                    DayOfWeek.MONDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.FRIDAY,
                    DayOfWeek.SUNDAY,
                )
            }
        }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        dayOrder.forEach { day ->
            Box(
                modifier = Modifier.height(cellSize),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (labeledDays.contains(day)) {
                    Text(
                        text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
                    )
                }
            }
        }
    }
}

private fun padDays(
    days: List<TaskProgressDay>,
    dayOrder: List<DayOfWeek>,
): List<TaskProgressDay?> {
    if (days.isEmpty()) {
        return emptyList()
    }

    val offset = dayOrder.indexOf(days.first().date.dayOfWeek).coerceAtLeast(0)

    return List(offset) { null } + days
}

@Composable
private fun rememberProgressPalette(flatColors: Boolean): ProgressPalette {
    val base = colorResource(id = R.color.md_teal_500)
    val muted = MaterialTheme.colors.onSurface.copy(alpha = 0.06f)
    val notDoneBase = colorResource(id = R.color.md_red_500)

    return remember(base, muted, notDoneBase, flatColors) {
        if (flatColors) {
            return@remember ProgressPalette(
                neutral = muted,
                doneScale = List(10) { base },
                notDoneScale = List(10) { notDoneBase },
            )
        }
        ProgressPalette(
            neutral = muted,
            doneScale =
                listOf(
                    muted,
                    base.copy(alpha = 0.2f),
                    base.copy(alpha = 0.3f),
                    base.copy(alpha = 0.4f),
                    base.copy(alpha = 0.5f),
                    base.copy(alpha = 0.6f),
                    base.copy(alpha = 0.7f),
                    base.copy(alpha = 0.8f),
                    base.copy(alpha = 0.9f),
                    base,
                ),
            notDoneScale =
                listOf(
                    muted,
                    notDoneBase.copy(alpha = 0.2f),
                    notDoneBase.copy(alpha = 0.3f),
                    notDoneBase.copy(alpha = 0.4f),
                    notDoneBase.copy(alpha = 0.5f),
                    notDoneBase.copy(alpha = 0.6f),
                    notDoneBase.copy(alpha = 0.7f),
                    notDoneBase.copy(alpha = 0.8f),
                    notDoneBase.copy(alpha = 0.9f),
                    notDoneBase,
                ),
        )
    }
}

private data class ProgressPalette(
    val neutral: Color,
    val doneScale: List<Color>,
    val notDoneScale: List<Color>,
)

private data class DayHeatmapMetadata(
    val color: Color,
    val semanticsDescription: String,
)

@Composable
private fun TaskProgressRange.toLabel(): String =
    when (this) {
        TaskProgressRange.ALL -> stringResource(R.string.all_days)
        TaskProgressRange.LAST_YEAR -> stringResource(R.string.last_12_months)
        TaskProgressRange.LAST_30_DAYS -> stringResource(R.string.last_30_days)
        TaskProgressRange.LAST_7_DAYS -> stringResource(R.string.last_7_days)
    }

private fun TaskProgressDay.color(
    palette: ProgressPalette,
    highlightNotDone: Boolean,
): Color {
    if (highlightNotDone && notDoneCount > 0) {
        val index = notDoneCount.coerceAtMost(palette.notDoneScale.lastIndex)
        return palette.notDoneScale[index]
    }
    if (doneCount <= 0) {
        return palette.neutral
    }
    val index = doneCount.coerceAtMost(palette.doneScale.lastIndex)
    return palette.doneScale[index]
}
