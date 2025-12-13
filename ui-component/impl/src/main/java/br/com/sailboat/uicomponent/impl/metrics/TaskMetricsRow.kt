package br.com.sailboat.uicomponent.impl.metrics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.sailboat.todozy.domain.model.TaskMetrics
import br.com.sailboat.uicomponent.impl.R
import br.com.sailboat.uicomponent.impl.theme.LocalTodozySemanticColors
import br.com.sailboat.uicomponent.impl.theme.LocalTodozySpacing

@Composable
fun TaskMetricsRow(
    taskMetrics: TaskMetrics,
    modifier: Modifier = Modifier,
    distributeEvenly: Boolean = true,
) {
    val spacing = LocalTodozySpacing.current
    val semanticColors = LocalTodozySemanticColors.current
    val doneTint = colorResource(id = R.color.md_teal_300)
    val notDoneTint = colorResource(id = R.color.md_red_300)
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.medium),
    ) {
        val metricModifier = if (distributeEvenly) Modifier.weight(1f) else Modifier
        if (taskMetrics.consecutiveDone > 0) {
            MetricIconWithValue(
                iconId = R.drawable.ic_fire_black_24dp,
                tint = semanticColors.warning,
                value = taskMetrics.consecutiveDone,
                modifier = metricModifier,
            )
        }
        MetricIconWithValue(
            iconId = R.drawable.ic_vec_thumb_up_white_24dp,
            tint = doneTint,
            value = taskMetrics.doneTasks,
            modifier = metricModifier,
        )
        MetricIconWithValue(
            iconId = R.drawable.ic_vect_thumb_down_white_24dp,
            tint = notDoneTint,
            value = taskMetrics.notDoneTasks,
            modifier = metricModifier,
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
