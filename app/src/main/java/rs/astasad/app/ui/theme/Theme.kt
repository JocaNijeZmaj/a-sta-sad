package rs.astasad.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AstaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AstaLightColors,
        typography = AstaTypography,
        shapes = AstaShapes,
        content = content
    )
}
