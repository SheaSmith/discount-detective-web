package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import getClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import org.jetbrains.skia.Image

@Composable
fun AsyncImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.FillBounds
) {
    var imageBitmap: ImageBitmap? by remember { mutableStateOf(null) }

    if (url != null) {
        LaunchedEffect("Image") {
            val client = HttpClient(getClient())
            val httpResponse: HttpResponse = client.get("http://localhost:8080/image-proxy?imageUrl=$url")
            client.close()
            val encodedImageData: ByteArray = httpResponse.body()
            val loadedImageBitmap: ImageBitmap = imageBitmapFromBytes(encodedImageData)
            imageBitmap = loadedImageBitmap
        }
    }

    Box(modifier = modifier) {
        AnimatedVisibility(visible = imageBitmap != null) {
            Image(
                bitmap = imageBitmap!!,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = contentScale
            )
        }
    }
}

fun imageBitmapFromBytes(encodedImageData: ByteArray): ImageBitmap {
    return Image.makeFromEncoded(encodedImageData).toComposeImageBitmap()
}