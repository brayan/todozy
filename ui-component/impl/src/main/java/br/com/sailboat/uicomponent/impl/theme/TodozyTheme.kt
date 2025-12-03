package br.com.sailboat.uicomponent.impl.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LightColorPalette =
    lightColors(
        primary = Color(0xFF2196F3), // md_blue_500
        primaryVariant = Color(0xFF1976D2), // md_blue_700
        secondary = Color(0xFF00BFA5), // md_teal_A700
        background = Color(0xFFF3F3F3), // default_background
        surface = Color.White,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onBackground = Color(0xFF212121), // primary_text
        onSurface = Color(0xFF212121),
    )

private val TodozyShapes =
    Shapes(
        small = androidx.compose.foundation.shape.RoundedCornerShape(6.dp),
        medium = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        large = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
    )

data class TodozySpacing(
    val xxsmall: androidx.compose.ui.unit.Dp = 2.dp,
    val xsmall: androidx.compose.ui.unit.Dp = 4.dp,
    val small: androidx.compose.ui.unit.Dp = 8.dp,
    val medium: androidx.compose.ui.unit.Dp = 16.dp,
    val large: androidx.compose.ui.unit.Dp = 24.dp,
    val xlarge: androidx.compose.ui.unit.Dp = 32.dp,
    val xxlarge: androidx.compose.ui.unit.Dp = 40.dp,
)

val LocalTodozySpacing = staticCompositionLocalOf { TodozySpacing() }

data class TodozySemanticColors(
    val success: Color = Color(0xFF009688), // md_teal_500
    val warning: Color = Color(0xFFFFA000), // amber_700-ish
    val error: Color = Color(0xFFF44336), // md_red_500
    val info: Color = Color(0xFF2196F3), // md_blue_500
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
