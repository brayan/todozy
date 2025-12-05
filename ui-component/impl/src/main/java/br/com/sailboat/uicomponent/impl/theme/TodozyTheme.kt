package br.com.sailboat.uicomponent.impl.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LightColorPalette =
    lightColors(
        primary = Color(0xFF2196F3),
        primaryVariant = Color(0xFF1976D2),
        secondary = Color(0xFF00BFA5),
        background = Color(0xFFF3F3F3),
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color(0xFF212121),
        onSurface = Color(0xFF212121),
    )

private val TodozyShapes =
    Shapes(
        small = RoundedCornerShape(6.dp),
        medium = RoundedCornerShape(12.dp),
        large = RoundedCornerShape(16.dp),
    )

data class TodozySpacing(
    val xxsmall: Dp = 2.dp,
    val xsmall: Dp = 4.dp,
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val xlarge: Dp = 32.dp,
    val xxlarge: Dp = 40.dp,
)

val LocalTodozySpacing = staticCompositionLocalOf { TodozySpacing() }

data class TodozySemanticColors(
    val success: Color = Color(0xFF009688),
    val warning: Color = Color(0xFFFFA000),
    val error: Color = Color(0xFFF44336),
    val info: Color = Color(0xFF2196F3),
)

val LocalTodozySemanticColors = staticCompositionLocalOf { TodozySemanticColors() }

val TodozyTypography =
    Typography(
        h4 = Typography().h4.copy(fontSize = 28.sp),
        h6 = Typography().h6.copy(fontSize = 20.sp),
        subtitle1 = Typography().subtitle1.copy(fontSize = 18.sp),
        body1 = Typography().body1.copy(fontSize = 16.sp),
        body2 = Typography().body2.copy(fontSize = 14.sp),
        caption = Typography().caption.copy(fontSize = 11.sp, letterSpacing = 0.sp),
        overline = Typography().overline.copy(fontSize = 11.sp, letterSpacing = 0.sp),
    )

@Composable
fun TodozyTheme(content: @Composable () -> Unit) {
    val spacing = TodozySpacing()
    val semantics = TodozySemanticColors()

    CompositionLocalProvider(
        LocalTodozySpacing provides spacing,
        LocalTodozySemanticColors provides semantics,
    ) {
        MaterialTheme(
            colors = LightColorPalette,
            typography = TodozyTypography,
            shapes = TodozyShapes,
            content = content,
        )
    }
}
