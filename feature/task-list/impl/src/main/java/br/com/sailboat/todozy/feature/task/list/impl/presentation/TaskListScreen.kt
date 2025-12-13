package br.com.sailboat.todozy.feature.task.list.impl.presentation

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableDefaults
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.snapTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.todozy.domain.model.TaskProgressDay
import br.com.sailboat.todozy.domain.model.TaskProgressRange
import br.com.sailboat.todozy.domain.model.TaskStatus
import br.com.sailboat.todozy.feature.task.list.impl.R
import br.com.sailboat.todozy.utility.kotlin.extension.isTrue
import br.com.sailboat.uicomponent.impl.metrics.TaskMetricsRow
import br.com.sailboat.uicomponent.impl.progress.TaskProgressContent
import br.com.sailboat.uicomponent.impl.skeleton.TaskSkeletonItem
import br.com.sailboat.uicomponent.impl.subhead.SubheadItem
import br.com.sailboat.uicomponent.impl.task.TaskItem
import br.com.sailboat.uicomponent.impl.theme.LocalTodozySpacing
import br.com.sailboat.uicomponent.impl.theme.TodozyTheme
import br.com.sailboat.uicomponent.model.SubheadUiModel
import br.com.sailboat.uicomponent.model.TaskSkeletonUiModel
import br.com.sailboat.uicomponent.model.TaskUiModel
import br.com.sailboat.uicomponent.model.UiModel
import kotlin.math.roundToInt
import br.com.sailboat.todozy.utility.android.R as AndroidUtilR
import br.com.sailboat.uicomponent.impl.R as UiR

private const val SWIPE_DISMISS_THRESHOLD_FRACTION = 0.6f

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
                                            onSwipe = { status ->
                                                onTaskSwipe(
                                                    item.taskId,
                                                    status,
                                                )
                                            },
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
) {
    val spacing = LocalTodozySpacing.current
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isSearchExpanded) {
        if (isSearchExpanded) {
            focusRequester.requestFocus()
            keyboardController?.show()
        } else {
            focusManager.clearFocus()
            keyboardController?.hide()
        }
    }
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
            }

            if (isSearchExpanded) {
                Spacer(modifier = Modifier.height(spacing.xsmall))
                TextField(
                    value = searchText,
                    onValueChange = onSearchChange,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
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
                    keyboardOptions =
                        KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                        ),
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
                TaskMetricsRow(
                    taskMetrics = it,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun SwipeableTaskItem(
    task: TaskUiModel,
    onClick: (Long) -> Unit,
    onUndoClick: (Long, TaskStatus) -> Unit,
    onSwipe: (TaskStatus) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalTodozySpacing.current
    val baseModifier = modifier
        .fillMaxWidth()
        .animateContentSize(animationSpec = tween(durationMillis = 200))

    AnchoredSwipeToDismiss(
        task = task,
        modifier = baseModifier,
        spacingHorizontal = spacing.medium,
        onSwipe = onSwipe,
        onClick = onClick,
        onUndoClick = onUndoClick,
        enableSwipe = task.showInlineMetrics.not(),
    )
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
private fun AnchoredSwipeToDismiss(
    task: TaskUiModel,
    modifier: Modifier = Modifier,
    spacingHorizontal: Dp,
    onSwipe: (TaskStatus) -> Unit,
    onClick: (Long) -> Unit,
    onUndoClick: (Long, TaskStatus) -> Unit,
    enableSwipe: Boolean,
) {
    var widthPx by remember { mutableFloatStateOf(0f) }
    var heightPx by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current

    val anchors = remember(widthPx) {
        if (widthPx == 0f) {
            DraggableAnchors<DismissValue> { DismissValue.Default at 0f }
        } else {
            DraggableAnchors<DismissValue> {
                DismissValue.Default at 0f
                DismissValue.DismissedToStart at -widthPx
                DismissValue.DismissedToEnd at widthPx
            }
        }
    }

    val dismissState = remember { AnchoredDraggableState(DismissValue.Default) }
    val flingBehavior = AnchoredDraggableDefaults.flingBehavior(
        state = dismissState,
        positionalThreshold = { distance -> distance * SWIPE_DISMISS_THRESHOLD_FRACTION },
    )

    LaunchedEffect(anchors) {
        dismissState.updateAnchors(anchors)
    }

    LaunchedEffect(task.taskId, task.inlineStatus, task.showInlineMetrics) {
        dismissState.snapTo(DismissValue.Default)
    }

    LaunchedEffect(dismissState.currentValue) {
        when (dismissState.currentValue) {
            DismissValue.DismissedToEnd -> {
                onSwipe(TaskStatus.DONE)
                dismissState.snapTo(DismissValue.Default)
            }

            DismissValue.DismissedToStart -> {
                onSwipe(TaskStatus.NOT_DONE)
                dismissState.snapTo(DismissValue.Default)
            }

            else -> Unit
        }
    }

    val offsetX = dismissState.offset.takeIf { !it.isNaN() } ?: 0f
    val direction =
        when {
            offsetX > 0f -> DismissDirection.StartToEnd
            offsetX < 0f -> DismissDirection.EndToStart
            else -> null
        }

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .onSizeChanged { size ->
                    widthPx = size.width.toFloat()
                    heightPx = size.height.toFloat()
                },
    ) {
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
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .let { base ->
                            if (heightPx > 0f) {
                                base.height(with(density) { heightPx.toDp() })
                            } else {
                                base
                            }
                        }
                        .background(backgroundColor)
                        .padding(horizontal = spacingHorizontal),
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

        TaskItem(
            task = task,
            onClick = onClick,
            onUndoClick = onUndoClick,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(offsetX.roundToInt(), 0) }
                    .anchoredDraggable(
                        state = dismissState,
                        orientation = Orientation.Horizontal,
                        enabled = enableSwipe,
                        flingBehavior = flingBehavior,
                    ),
        )
    }
}
