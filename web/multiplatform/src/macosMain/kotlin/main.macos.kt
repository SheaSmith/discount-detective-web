import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import com.example.cosc345project.ui.screens.MainScreen
import com.example.cosc345project.ui.theme.DiscountDetectiveTheme
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import moe.tlaster.precompose.PreComposeWindow
import platform.AppKit.NSApp
import platform.AppKit.NSApplication

fun main() {
    NSApplication.sharedApplication()
    PreComposeWindow(
        "Discount Detective",
        onCloseRequest = {
            NSApp?.terminate(null)
        }
    ) {
        DiscountDetectiveTheme {
            MainScreen(WindowWidthSizeClass.Expanded)
        }
    }
    NSApp?.run()
}

@Composable
actual fun AlertDialog2(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    text: @Composable () -> Unit,
    title: @Composable () -> Unit,
    icon: @Composable () -> Unit
) {}