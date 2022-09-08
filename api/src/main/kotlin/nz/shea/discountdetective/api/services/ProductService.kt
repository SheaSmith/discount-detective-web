package nz.shea.discountdetective.api.services

import com.example.cosc345.shared.models.Product
import nz.shea.discountdetective.api.data.PageDTO
import nz.shea.discountdetective.api.data.StorageProduct
import nz.shea.discountdetective.api.repositories.ProductRepository
import org.hibernate.search.engine.search.query.SearchResult
import org.hibernate.search.mapper.orm.Search
import org.springframework.stereotype.Service
import javax.persistence.EntityManager
import javax.persistence.TypedQuery
import kotlin.jvm.optionals.getOrNull


@Service
class ProductService(private val db: ProductRepository, private val entityManager: EntityManager) {
    fun searchProducts(query: String, offset: Int): PageDTO<Map<String, Product>> {
        val total: Long
        val products: Map<String, Product>

        if (query.isNotBlank()) {
            val searchSession = Search.session(entityManager)

            val result: SearchResult<StorageProduct> = searchSession
                .search(StorageProduct::class.java)
                .where { f -> f.match().fields("information.name").matching(query).fuzzy(2) }
                .fetch(offset, 30) as SearchResult<StorageProduct>

            products = result.hits().associate { it.toProduct() }
            total = result.total().hitCount()
        } else {
            val query: TypedQuery<StorageProduct> =
                entityManager.createQuery("SELECT p FROM StorageProduct p ORDER BY p.id", StorageProduct::class.java)
            query.firstResult = offset
            query.maxResults = 30

            products = query.resultList.associate { it.toProduct() }
            total = db.count()
        }

        return PageDTO(products, total)
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun findById(id: String): Product? = db.findById(id).getOrNull()?.toProduct()?.second
    //= db.searchProducts(query)

    fun addAll(products: Map<String, Product>) =
        db.saveAll(products.map { StorageProduct(it.value, it.key) })

    fun deleteAll() = db.deleteAll()
}