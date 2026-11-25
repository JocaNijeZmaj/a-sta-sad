package rs.astasad.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import rs.astasad.app.navigation.AstaNavHost
import rs.astasad.app.ui.theme.AstaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lightSystemBar = SystemBarStyle.auto(
            lightScrim = Color.TRANSPARENT,
            darkScrim = Color.BLACK,
            detectDarkMode = { false }
        )
        enableEdgeToEdge(statusBarStyle = lightSystemBar, navigationBarStyle = lightSystemBar)
        setContent {
            AstaTheme {
                AstaNavHost()
            }
        }
    }
}
