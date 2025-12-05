package br.com.sailboat.uicomponent.impl.subhead

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.sailboat.uicomponent.impl.theme.LocalTodozySpacing
import java.util.Locale

@Composable
fun SubheadItem(
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
                .height(48.dp)
                .padding(start = spacing.medium),
        style = MaterialTheme.typography.overline.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colors.secondary,
    )
}
