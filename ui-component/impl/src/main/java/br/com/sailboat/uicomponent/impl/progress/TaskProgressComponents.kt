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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.sailboat.todozy.domain.model.TaskProgressDay
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.uicomponent.impl.R
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

@Composable
internal fun TaskProgressContent(
    days: List<TaskProgressDay>,
    selectedRange: TaskProgressRange,
    onRangeSelected: (TaskProgressRange) -> Unit,
    onDayClick: ((TaskProgressDay) -> Unit)?,
    isLoading: Boolean = false,
    enableDayDetails: Boolean = false,
) {
    val palette = rememberProgressPalette()
    val totalDone = remember(days) { days.sumOf { it.doneCount } }
    val formatter = remember { DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM) }
    val (selectedDay, onSelectDay) = remember { mutableStateOf<TaskProgressDay?>(null) }
    val haptic = LocalHapticFeedback.current

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
            TaskProgressSkeleton()
        } else {
            Text(
                text = stringResource(R.string.tasks_done_format, totalDone),
                style =
                    MaterialTheme.typography.caption.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                    ),
                color = colorResource(id = R.color.md_blue_grey_700),
            )

            Spacer(modifier = Modifier.size(8.dp))

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
                onDayClick = dayClickHandler,
                onDayHaptic = { haptic.performHapticFeedback(HapticFeedbackType.LongPress) },
                selectedDay = selectedDay,
                onDismissTooltip = { onSelectDay(null) },
                formatter = formatter,
                enableDayDetails = enableDayDetails,
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
            Surface(
                modifier =
                    Modifier.clickable {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onRangeSelected(range)
                    },
                color =
                    if (selected) {
                        colorResource(id = R.color.md_teal_100)
                    } else {
                        MaterialTheme.colors.surface
                    },
                shape = RoundedCornerShape(12.dp),
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
    palette: List<Color>,
    onDayClick: ((TaskProgressDay) -> Unit)?,
    onDayHaptic: (() -> Unit)?,
    selectedDay: TaskProgressDay?,
    onDismissTooltip: () -> Unit,
    formatter: DateTimeFormatter,
    enableDayDetails: Boolean,
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
                    val description = "$label ${day.date} (${day.doneCount}/${day.totalCount})"
                    DayHeatmapMetadata(
                        color = palette[day.paletteIndex()],
                        semanticsDescription = description,
                    )
                },
            )
        }

    val paddedDays = remember(days) { padDays(days) }
    val weeks = remember(paddedDays) { paddedDays.chunked(7) }
    val lastWeekIndex = remember(weeks.size) { weeks.lastIndex.coerceAtLeast(0) }
    val lazyListState: LazyListState =
        rememberSaveable(weeks.size, saver = LazyListState.Saver) {
            LazyListState(lastWeekIndex)
        }

    Row(verticalAlignment = Alignment.Top) {
        WeekdayLabels()

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            state = lazyListState,
        ) {
            items(weeks) { week ->
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    week.forEach { day ->
                        val metadata = day?.let { dayMetadata[it.date] }
                        val color = metadata?.color ?: palette.first()
                        val semanticsDescription = metadata?.semanticsDescription

                        Box(
                            modifier =
                                Modifier
                                    .size(16.dp)
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
private fun TaskProgressSkeleton() {
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
            WeekdayLabels()

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(5) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        repeat(7) {
                            Box(
                                modifier =
                                    Modifier
                                        .size(16.dp)
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
private fun WeekdayLabels() {
    val dayOrder =
        remember {
            listOf(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY,
            )
        }
    val labeledDays =
        remember {
            setOf(
                DayOfWeek.MONDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SUNDAY,
            )
        }
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        dayOrder.forEach { day ->
            Box(
                modifier = Modifier.height(16.dp),
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

private fun padDays(days: List<TaskProgressDay>): List<TaskProgressDay?> {
    if (days.isEmpty()) {
        return emptyList()
    }

    val dayOfWeekOrder =
        listOf(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY,
            DayOfWeek.SUNDAY,
        )
    val offset = dayOfWeekOrder.indexOf(days.first().date.dayOfWeek).coerceAtLeast(0)

    return List(offset) { null } + days
}

@Composable
private fun rememberProgressPalette(): List<Color> {
    val base = colorResource(id = R.color.md_teal_500)
    val muted = MaterialTheme.colors.onSurface.copy(alpha = 0.06f)

    return remember(base, muted) {
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
        )
    }
}

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

private fun TaskProgressDay.paletteIndex(): Int {
    if (doneCount <= 0) {
        return 0
    }
    return doneCount.coerceAtMost(9)
}
