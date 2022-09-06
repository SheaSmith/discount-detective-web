package repository

import getClient
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

abstract class RepositoryBase {
    protected val client = HttpClient(getClient()) {
        install(ContentNegotiation) {
            json()
        }
    }

    protected val baseUrl = "http://localhost:8080"
}