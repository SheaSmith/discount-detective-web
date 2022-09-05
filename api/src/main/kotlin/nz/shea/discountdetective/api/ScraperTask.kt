package nz.shea.discountdetective.api

import com.example.cosc345.scraper.Matcher
import kotlinx.coroutines.runBlocking
import nz.shea.discountdetective.api.data.StorageProduct
import nz.shea.discountdetective.api.services.ProductService
import nz.shea.discountdetective.api.services.RetailerService
import org.hibernate.search.mapper.orm.Search
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@Component
class ScraperTask(
    private val productService: ProductService,
    private val retailerService: RetailerService,
    private val entityManager: EntityManager
) {

    @Transactional
    @Scheduled(cron = "0 2 * * *")
    @EventListener(ApplicationReadyEvent::class)
    fun runScrapers() {
        val matcher = Matcher()

        runBlocking {
            val result = matcher.runScrapers()

            val barcodeResults = matcher.matchBarcodes(result.second.toMutableList(), result.first)
            val valueResults = matcher.matchNames(barcodeResults.second, barcodeResults.first)

            productService.deleteAll()
            retailerService.deleteAll()
            retailerService.insertAll(valueResults.first)
            productService.addAll(valueResults.second)

            val searchSession = Search.session(entityManager)

            val indexer = searchSession.massIndexer(StorageProduct::class.java)
                .threadsToLoadObjects(7)

            indexer.startAndWait()
        }
    }
}