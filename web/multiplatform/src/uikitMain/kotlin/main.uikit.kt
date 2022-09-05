import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Application
import com.example.cosc345project.ui.screens.MainScreen
import com.example.cosc345project.ui.theme.DiscountDetectiveTheme
import kotlinx.cinterop.*
import moe.tlaster.precompose.PreComposeApplication
import platform.Foundation.*
import platform.UIKit.*

fun main() {
    val args = emptyArray<String>()
    memScoped {
        val argc = args.size + 1
        val argv = (arrayOf("skikoApp") + args).map { it.cstr.ptr }.toCValues()
        autoreleasepool {
            UIApplicationMain(argc, argv, null, NSStringFromClass(SkikoAppDelegate))
        }
    }
}

class SkikoAppDelegate : UIResponder, UIApplicationDelegateProtocol {
    companion object : UIResponderMeta(), UIApplicationDelegateProtocolMeta

    @ObjCObjectBase.OverrideInit
    constructor() : super()

    private var _window: UIWindow? = null
    override fun window() = _window
    override fun setWindow(window: UIWindow?) {
        _window = window
    }

    override fun application(
        application: UIApplication,
        didFinishLaunchingWithOptions: Map<Any?, *>?
    ): Boolean {
        window = UIWindow(frame = UIScreen.mainScreen.bounds)
        window!!.rootViewController = PreComposeApplication("Discount Detective") {
            DiscountDetectiveTheme {
                MainScreen(WindowWidthSizeClass.Compact)
            }
        }
        window!!.makeKeyAndVisible()
        return true
    }
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