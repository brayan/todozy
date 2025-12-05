package br.com.sailboat.todozy.feature.task.list.impl.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskProgressDay
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.list.impl.R
import br.com.sailboat.todozy.utility.kotlin.extension.isTrue
import br.com.sailboat.uicomponent.impl.progress.TaskProgressContent
import br.com.sailboat.uicomponent.impl.skeleton.TaskSkeletonItem
import br.com.sailboat.uicomponent.impl.subhead.SubheadItem
import br.com.sailboat.uicomponent.impl.task.TaskItem
import br.com.sailboat.uicomponent.impl.theme.LocalTodozySemanticColors
import br.com.sailboat.uicomponent.impl.theme.LocalTodozySpacing
import br.com.sailboat.uicomponent.impl.theme.TodozyTheme
import br.com.sailboat.uicomponent.model.SubheadUiModel
import br.com.sailboat.uicomponent.model.TaskSkeletonUiModel
import br.com.sailboat.uicomponent.model.TaskUiModel
import br.com.sailboat.uicomponent.model.UiModel
import br.com.sailboat.todozy.utility.android.R as AndroidUtilR
import br.com.sailboat.uicomponent.impl.R as UiR

@Composable
internal fun TaskListScreen(
    tasksLoading: Boolean,
    items: List<UiModel>,
    taskMetrics: TaskMetrics?,
    taskProgressDays: List<TaskProgressDay>,
    taskProgressRange: TaskProgressRange,
    taskProgressLoading: Boolean,
    onSelectProgressRange: (TaskProgressRange) -> Unit,
    onTaskClick: (Long) -> Unit,
    onTaskSwipe: (taskId: Long, TaskStatus) -> Unit,
    onTaskUndo: (Long, TaskStatus) -> Unit,
    onNewTask: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenSettings: () -> Unit,
    onSearch: (String) -> Unit,
) {
    TodozyTheme {
        val spacing = LocalTodozySpacing.current
        var search by rememberSaveable { mutableStateOf("") }
        var isSearchExpanded by rememberSaveable { mutableStateOf(false) }
        val metricsToShow = if (tasksLoading.isTrue()) null else taskMetrics
        val listState = rememberLazyListState()
        var pendingScrollToTop by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(tasksLoading, taskProgressLoading) {
            if (tasksLoading || taskProgressLoading) {
                pendingScrollToTop = true
            } else if (pendingScrollToTop) {
                listState.scrollToItem(0)
                pendingScrollToTop = false
            }
        }

        Scaffold(
            topBar = {
                TaskListTopBar(
                    taskMetrics = metricsToShow,
                    searchText = search,
                    isSearchExpanded = isSearchExpanded,
                    onSearchChange = { query ->
                        search = query
                        onSearch(query)
                    },
                    onToggleSearch = { isSearchExpanded = it },
                    onOpenHistory = onOpenHistory,
                    onOpenSettings = onOpenSettings,
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = onNewTask,
                    icon = { Icon(imageVector = Icons.Default.Add, contentDescription = null) },
                    text = { Text(text = stringResource(id = R.string.fab_new_task)) },
                    backgroundColor = MaterialTheme.colors.primary,
                    contentColor = MaterialTheme.colors.onPrimary,
                )
            },
        ) { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            bottom = spacing.xxlarge + spacing.large,
                            top = spacing.small,
                        ),
                        verticalArrangement = Arrangement.spacedBy(spacing.small),
                    ) {
                        item(key = "progress") {
                            TaskProgressContent(
                                modifier = Modifier.padding(
                                    horizontal = spacing.medium,
                                    vertical = spacing.small,
                                ),
                                days = taskProgressDays,
                                selectedRange = taskProgressRange,
                                onRangeSelected = onSelectProgressRange,
                                onDayClick = {},
                                isLoading = taskProgressLoading,
                                enableDayDetails = true,
                                flatColors = false,
                            )
                        }

                        if (tasksLoading) {
                            items(5) { TaskSkeletonItem() }
                        } else if (items.isEmpty()) {
                            item {
                                EmptyState()
                            }
                        } else {
                            itemsIndexed(
                                items = items,
                                key = { index, item -> stableTaskListKey(item, index) },
                            ) { index, item ->
                                when (item) {
                                    is TaskUiModel ->
                                        SwipeableTaskItem(
                                            task = item,
                                            onClick = onTaskClick,
                                            onUndoClick = onTaskUndo,
                                            onSwipe = { status -> onTaskSwipe(item.taskId, status) },
                                            modifier = Modifier.animateItem(),
                                        )

                                    is SubheadUiModel ->
                                        SubheadItem(
                                            text = item.subhead,
                                            modifier = Modifier.animateItem(),
                                        )

                                    is TaskSkeletonUiModel ->
                                        TaskSkeletonItem(
                                            modifier = Modifier.animateItem(),
                                        )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskListTopBar(
    taskMetrics: TaskMetrics?,
    searchText: String,
    isSearchExpanded: Boolean,
    onSearchChange: (String) -> Unit,
    onToggleSearch: (Boolean) -> Unit,
    onOpenHistory: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    val spacing = LocalTodozySpacing.current
    Surface(
        color = colorResource(id = UiR.color.md_blue_500),
        elevation = 4.dp,
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = spacing.medium, vertical = spacing.small),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(id = UiR.string.label_tasks),
                    style = MaterialTheme.typography.h6,
                    color = Color.White,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = { onToggleSearch(!isSearchExpanded) }) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = stringResource(id = AndroidUtilR.string.search),
                        tint = Color.White,
                    )
                }
                IconButton(onClick = onOpenHistory) {
                    Icon(
                        Icons.Default.History,
                        contentDescription = stringResource(id = UiR.string.history),
                        tint = Color.White,
                    )
                }
                IconButton(onClick = onOpenSettings) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = stringResource(id = UiR.string.settings),
                        tint = Color.White,
                    )
                }
            }

            if (isSearchExpanded) {
                Spacer(modifier = Modifier.height(spacing.xsmall))
                TextField(
                    value = searchText,
                    onValueChange = onSearchChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text(text = stringResource(id = AndroidUtilR.string.search)) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f),
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (searchText.isNotEmpty()) {
                                    onSearchChange("")
                                } else {
                                    onToggleSearch(false)
                                }
                            },
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = null,
                            )
                        }
                    },
                    colors =
                        TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            textColor = MaterialTheme.colors.onSurface,
                            cursorColor = MaterialTheme.colors.primary,
                        ),
                )
            }

            taskMetrics?.let {
                Spacer(modifier = Modifier.height(spacing.small))
                MetricsRow(taskMetrics = it)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SwipeableTaskItem(
    task: TaskUiModel,
    onClick: (Long) -> Unit,
    onUndoClick: (Long, TaskStatus) -> Unit,
    onSwipe: (TaskStatus) -> Unit,
    modifier: Modifier = Modifier,
) {
    val semanticColors = LocalTodozySemanticColors.current
    val spacing = LocalTodozySpacing.current
    val dismissState =
        androidx.compose.material.rememberDismissState(
            confirmStateChange = { newValue ->
                when (newValue) {
                    DismissValue.DismissedToEnd -> onSwipe(TaskStatus.DONE)
                    DismissValue.DismissedToStart -> onSwipe(TaskStatus.NOT_DONE)
                    else -> Unit
                }
                false
            },
        )

    if (task.showInlineMetrics) {
        TaskItem(
            task = task,
            onClick = onClick,
            onUndoClick = onUndoClick,
            modifier = modifier,
        )
    } else {
        SwipeToDismiss(
            state = dismissState,
            directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
            dismissThresholds = { FractionalThreshold(0.6f) },
            background = {
                val direction = dismissState.dismissDirection
                val (icon, backgroundColor) =
                    when (direction) {
                        DismissDirection.StartToEnd ->
                            UiR.drawable.ic_vec_thumb_up_white_24dp to colorResource(id = UiR.color.md_teal_200)

                        DismissDirection.EndToStart ->
                            UiR.drawable.ic_vect_thumb_down_white_24dp to colorResource(id = UiR.color.md_red_200)

                        else -> null to Color.Transparent
                    }

                if (icon != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(backgroundColor)
                            .padding(horizontal = spacing.medium),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement =
                            when (direction) {
                                DismissDirection.StartToEnd -> Arrangement.Start
                                DismissDirection.EndToStart -> Arrangement.End
                                else -> Arrangement.Start
                            },
                    ) {
                        Icon(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            tint = Color.White,
                        )
                    }
                }
            },
            modifier = modifier,
        ) {
            TaskItem(
                task = task,
                onClick = onClick,
                onUndoClick = onUndoClick,
            )
        }
    }
}

@Composable
private fun MetricsRow(taskMetrics: TaskMetrics) {
    val spacing = LocalTodozySpacing.current
    val semanticColors = LocalTodozySemanticColors.current
    val doneTint = colorResource(id = UiR.color.md_teal_300)
    val notDoneTint = colorResource(id = UiR.color.md_red_300)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.medium),
    ) {
        if (taskMetrics.consecutiveDone > 0) {
            MetricIconWithValue(
                iconId = UiR.drawable.ic_fire_black_24dp,
                tint = semanticColors.warning,
                value = taskMetrics.consecutiveDone,
                modifier = Modifier.weight(1f),
            )
        }
        MetricIconWithValue(
            iconId = UiR.drawable.ic_vec_thumb_up_white_24dp,
            tint = doneTint,
            value = taskMetrics.doneTasks,
            modifier = Modifier.weight(1f),
        )
        MetricIconWithValue(
            iconId = UiR.drawable.ic_vect_thumb_down_white_24dp,
            tint = notDoneTint,
            value = taskMetrics.notDoneTasks,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun MetricIconWithValue(
    iconId: Int,
    tint: Color,
    value: Int,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalTodozySpacing.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.xsmall),
    ) {
        Surface(
            shape = CircleShape,
            color = Color.White,
            elevation = 0.dp,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(28.dp)
                        .padding(spacing.xxsmall),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.height(18.dp),
                )
            }
        }
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.subtitle1,
            color = Color.White,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun EmptyState() {
    val spacing = LocalTodozySpacing.current
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = UiR.string.no_tasks),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface,
        )
        Text(
            text = stringResource(id = UiR.string.ept_click_to_add),
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
        )
    }
}

private fun stableTaskListKey(
    item: UiModel,
    index: Int,
): String = when (item) {
    is TaskUiModel -> "task-${item.taskId}"
    is TaskSkeletonUiModel -> "task-skeleton-${item.placeholderId}"
    is SubheadUiModel -> "subhead-${item.subhead}"
    else -> "${item.uiModelId}-$index"
}
