package nz.shea.discountdetective.api

import com.example.cosc345.shared.models.Product
import com.example.cosc345.shared.models.Retailer
import nz.shea.discountdetective.api.services.ProductService
import nz.shea.discountdetective.api.services.RetailerService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class ApiApplication

fun main(args: Array<String>) {
	runApplication<ApiApplication>(*args)
}

@RestController
@RequestMapping("/products")
class ProductResource(val service: ProductService, val component: ScraperTask) {
	@RequestMapping("/search")
	fun search(query: String): Map<String, Product> {
		return service.searchProducts(query)
	}

	@RequestMapping("/run-scraper")
	fun runScraper() {
		component.runScrapers()
	}
}

@RestController
class RetailerResource(val service: RetailerService) {
	@GetMapping
	fun index(): Map<String, Retailer> = service.findRetailers()
}