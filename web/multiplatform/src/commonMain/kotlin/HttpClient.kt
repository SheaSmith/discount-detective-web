import io.ktor.client.engine.*

expect fun getClient(): HttpClientEngineFactory<HttpClientEngineConfig>