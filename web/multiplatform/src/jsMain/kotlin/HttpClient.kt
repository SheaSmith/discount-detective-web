import io.ktor.client.engine.*
import io.ktor.client.engine.js.*

actual fun getClient(): HttpClientEngineFactory<HttpClientEngineConfig> = Js