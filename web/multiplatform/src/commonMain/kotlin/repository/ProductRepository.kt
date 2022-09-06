package repository

import io.ktor.client.call.*
import io.ktor.client.request.*
import models.Product
import nz.shea.discountdetective.api.data.PageDTO

class ProductRepository : RepositoryBase() {
    suspend fun search(query: String, offset: Int = 0): PageDTO<Map<String, Product>> {
        return client.get("${baseUrl}/products/search?query=${query}&offset=${offset}").body()
    }

    suspend fun getProduct(id: String): Product {
        return client.get("${baseUrl}/products/${id}").body()
    }
}