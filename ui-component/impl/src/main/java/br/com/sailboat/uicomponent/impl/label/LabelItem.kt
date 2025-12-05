package br.com.sailboat.uicomponent.impl.label

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import br.com.sailboat.uicomponent.impl.theme.LocalTodozySpacing
import java.util.Locale

@Composable
internal fun LabelItem(
    text: String,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalTodozySpacing.current
    val displayText = remember(text) { text.uppercase(Locale.getDefault()) }

    Text(
        text = displayText,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(start = spacing.medium, top = spacing.small, bottom = spacing.xsmall),
        style = MaterialTheme.typography.overline.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colors.secondary,
    )
}
