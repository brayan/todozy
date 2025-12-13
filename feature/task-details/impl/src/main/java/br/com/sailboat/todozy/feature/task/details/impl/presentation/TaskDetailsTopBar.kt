package br.com.sailboat.todozy.feature.task.details.impl.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.uicomponent.impl.metrics.TaskMetricsRow
import br.com.sailboat.uicomponent.impl.theme.LocalTodozySpacing
import br.com.sailboat.uicomponent.impl.R as UiR

@Composable
internal fun TaskDetailsTopBar(
    taskMetrics: TaskMetrics?,
    onNavigateBack: () -> Unit,
    onClickDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalTodozySpacing.current
    var isMenuExpanded by remember { mutableStateOf(false) }
    Surface(
        color = colorResource(id = UiR.color.md_blue_500),
        elevation = 4.dp,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(vertical = spacing.small),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    painter = painterResource(id = UiR.drawable.ic_arrow_back_white_24dp),
                    contentDescription = null,
                    tint = Color.White,
                )
            }
            if (taskMetrics == null) {
                Text(
                    text = stringResource(id = UiR.string.task_details),
                    style = MaterialTheme.typography.h6,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
            } else {
                TaskMetricsRow(
                    taskMetrics = taskMetrics,
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(horizontal = spacing.small),
                )
            }
            Box {
                IconButton(onClick = { isMenuExpanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null,
                        tint = Color.White,
                    )
                }
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                ) {
                    DropdownMenuItem(
                        onClick = {
                            isMenuExpanded = false
                            onClickDelete()
                        },
                    ) {
                        Text(text = stringResource(id = UiR.string.delete))
                    }
                }
            }
        }
    }
}
