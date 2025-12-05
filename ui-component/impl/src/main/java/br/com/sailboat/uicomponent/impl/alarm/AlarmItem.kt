package br.com.sailboat.uicomponent.impl.alarm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import br.com.sailboat.uicomponent.impl.R
import br.com.sailboat.uicomponent.impl.theme.LocalTodozySemanticColors
import br.com.sailboat.uicomponent.impl.theme.LocalTodozySpacing

@Composable
internal fun AlarmItem(
    date: String,
    time: String,
    repeatDescription: String?,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalTodozySpacing.current
    val semanticColors = LocalTodozySemanticColors.current

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = spacing.medium, vertical = spacing.small),
    ) {
        Text(
            text = date,
            style =
                MaterialTheme.typography.h6.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal,
                ),
            color = colorResource(id = R.color.md_blue_300),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        Spacer(modifier = Modifier.height(spacing.xsmall))

        Text(
            text = time,
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Light),
            color = colorResource(id = R.color.md_teal_300),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        if (!repeatDescription.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(spacing.xsmall))
            Text(
                text = repeatDescription,
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Normal),
                color = colorResource(id = R.color.md_blue_grey_300),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
