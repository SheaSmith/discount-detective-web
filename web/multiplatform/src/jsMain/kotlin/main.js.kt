// calling internal functions ComposeLayer and ComposeWindow
@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "EXPOSED_PARAMETER_TYPE")

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.native.ComposeLayer
import androidx.compose.ui.window.ComposeWindow
import androidx.compose.ui.window.Window
import com.example.cosc345project.ui.screens.MainScreen
import com.example.cosc345project.ui.theme.DiscountDetectiveTheme
import io.ktor.client.engine.*
import io.ktor.client.engine.js.*
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import moe.tlaster.precompose.preComposeWindow
import org.jetbrains.skiko.wasm.onWasmReady
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.WheelEvent

var canvas = document.getElementById("ComposeTarget") as HTMLCanvasElement

fun canvasResize(width: Int = window.innerWidth, height: Int = window.innerHeight) {
    canvas.setAttribute("width", "$width")
    canvas.setAttribute("height", "$height")
}

fun composableResize(layer: ComposeLayer) {
    val clone = canvas.cloneNode(false) as HTMLCanvasElement
    canvas.replaceWith(clone)
    canvas = clone

    val scale = layer.layer.contentScale
    canvasResize()
    layer.layer.attachTo(clone)
    layer.layer.needRedraw()
    layer.setSize(
        (clone.width / scale).toInt(),
        (clone.height / scale).toInt()
    )
}

@OptIn(DelicateCoroutinesApi::class)
fun main() {
    val scrollListener = Channel<Double>()

    window.addEventListener("wheel", { event ->
        if (event is WheelEvent) {
            event.stopPropagation()
            GlobalScope.launch {
                println("Scroll")
                scrollListener.trySend(event.deltaY)
            }
        }
    })

    onWasmReady {
        canvasResize()
        preComposeWindow {
            var sizeClass by mutableStateOf(calculateClass())

            window.addEventListener("resize", {
                composableResize(layer = layer)
                sizeClass = calculateClass()
            })
            DiscountDetectiveTheme {
                MainScreen(sizeClass, scrollListener)
            }

        }
    }
}

private fun calculateClass(): WindowWidthSizeClass {
    return if (window.innerWidth <= 599) {
        WindowWidthSizeClass.Compact
    } else if (window.innerWidth <= 839) {
        WindowWidthSizeClass.Medium
    } else {
        WindowWidthSizeClass.Expanded
    }
}