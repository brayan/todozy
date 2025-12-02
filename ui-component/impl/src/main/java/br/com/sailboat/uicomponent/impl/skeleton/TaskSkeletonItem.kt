package br.com.sailboat.uicomponent.impl.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
@Suppress("FunctionName")
fun TaskSkeletonItem() {
    val baseColor = Color(0xFFE6E6E6)
    val cardColor = Color.White

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
                .heightIn(min = 72.dp)
                .background(color = cardColor, shape = RoundedCornerShape(12.dp))
                .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier =
                Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .background(color = baseColor, shape = RoundedCornerShape(20.dp)),
        )

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.Start,
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.75f)
                        .height(14.dp)
                        .background(color = baseColor, shape = RoundedCornerShape(8.dp)),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth(0.5f)
                        .height(12.dp)
                        .background(color = baseColor, shape = RoundedCornerShape(8.dp)),
            )
        }

        Box(
            modifier =
                Modifier
                    .width(64.dp)
                    .height(20.dp)
                    .background(color = baseColor, shape = RoundedCornerShape(6.dp)),
        )
    }
}
