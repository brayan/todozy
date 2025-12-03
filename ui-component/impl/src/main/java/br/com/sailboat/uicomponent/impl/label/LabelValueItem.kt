package br.com.sailboat.uicomponent.impl.label

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import br.com.sailboat.uicomponent.impl.theme.LocalTodozySpacing

@Composable
internal fun LabelValueItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalTodozySpacing.current

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(start = spacing.medium, bottom = spacing.xxlarge),
    ) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.overline.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colors.secondary,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = spacing.xsmall, end = spacing.medium),
        )
    }
}
