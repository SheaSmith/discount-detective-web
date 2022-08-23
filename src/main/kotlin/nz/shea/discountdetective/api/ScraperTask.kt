package nz.shea.discountdetective.api

import com.example.cosc345.scraper.Matcher
import kotlinx.coroutines.runBlocking
import nz.shea.discountdetective.api.services.ProductService
import nz.shea.discountdetective.api.services.RetailerService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScraperTask(private val productService: ProductService, private val retailerService: RetailerService) {

    @Scheduled(cron = "0 2 * * *")
    fun runScrapers() {
        val matcher = Matcher()

        runBlocking {
            val result = matcher.runScrapers()

            val barcodeResults = matcher.matchBarcodes(result.second.toMutableList(), result.first)
            val valueResults = matcher.matchNames(barcodeResults.second, barcodeResults.first)

            retailerService.insertAll(valueResults.first)
            productService.addAll(valueResults.second)
        }
    }
}