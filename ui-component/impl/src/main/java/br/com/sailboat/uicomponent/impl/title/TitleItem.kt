package br.com.sailboat.uicomponent.impl.title

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.sailboat.uicomponent.impl.R
import br.com.sailboat.uicomponent.impl.theme.LocalTodozySpacing

@Composable
internal fun TitleItem(
    text: String,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalTodozySpacing.current

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = text,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = spacing.medium,
                        end = spacing.medium,
                        top = spacing.large,
                        bottom = spacing.small,
                    ),
            textAlign = TextAlign.Center,
            style =
                MaterialTheme.typography.h6.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal,
                ),
            color = colorResource(id = R.color.md_blue_grey_500),
        )

        Spacer(modifier = Modifier.height(spacing.xsmall))

        DiamondDivider(
            color = MaterialTheme.colors.secondary,
            modifier =
                Modifier
                    .padding(
                        start = spacing.medium,
                        end = spacing.medium,
                        bottom = spacing.medium,
                    ),
        )
    }
}

@Composable
private fun DiamondDivider(
    color: Color,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalTodozySpacing.current

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .height(24.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(color),
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_diamond_white_24dp),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp).padding(horizontal = spacing.xsmall),
        )

        Box(
            modifier =
                Modifier
                    .weight(1f)
                    .height(1.dp)
                    .background(color),
        )
    }
}
