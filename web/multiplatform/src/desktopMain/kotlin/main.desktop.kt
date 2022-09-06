import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.singleWindowApplication
import com.example.cosc345project.ui.screens.MainScreen
import com.example.cosc345project.ui.theme.DiscountDetectiveTheme
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import moe.tlaster.precompose.PreComposeWindow

fun main() {
    application {
        PreComposeWindow(
            title = "Discount Detective",
            onCloseRequest = {
                exitApplication()
            },
        ) {
            DiscountDetectiveTheme {
                MainScreen(WindowWidthSizeClass.Expanded)
            }
        }
    }
}