import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*

actual fun getClient(): HttpClientEngineFactory<HttpClientEngineConfig> = CIO