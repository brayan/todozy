package br.com.sailboat.uicomponent.impl.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.sailboat.uicomponent.impl.theme.LocalTodozySpacing

@Composable
internal fun ImageTitleDividerItem(
    imageRes: Int,
    title: String,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalTodozySpacing.current
    val dividerColor = MaterialTheme.colors.secondary

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(
                    start = spacing.medium,
                    end = spacing.medium,
                    top = spacing.medium,
                    bottom = spacing.small,
                ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.size(48.dp),
        )

        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colors.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(start = spacing.small),
        )
    }

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.medium, vertical = spacing.small),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(dividerColor),
        )
        Spacer(modifier = Modifier.padding(horizontal = spacing.small))
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(dividerColor),
        )
    }
}
