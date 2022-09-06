package repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import models.Retailer

class RetailerRepository : RepositoryBase() {
    suspend fun getRetailers(): Map<String, Retailer> {
        return client.get("${baseUrl}/retailers").body()
    }
}