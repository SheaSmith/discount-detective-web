package nz.shea.discountdetective.api

import com.example.cosc345.shared.models.Product
import com.example.cosc345.shared.models.Retailer
import nz.shea.discountdetective.api.data.PageDTO
import nz.shea.discountdetective.api.services.ProductService
import nz.shea.discountdetective.api.services.RetailerService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
	fun search(query: String, offset: Int = 0): PageDTO<Map<String, Product>> {
		return service.searchProducts(query, offset)
	}

	@RequestMapping("/{id}")
	fun getProduct(@PathVariable("id") id: String): Product? {
		return service.findById(id)
	}

	@RequestMapping("/run-scraper")
	fun runScraper() {
		component.runScrapers()
	}
}

@RestController
@RequestMapping("/retailers")
class RetailerResource(val service: RetailerService) {
	@RequestMapping("/")
	@GetMapping
	fun index(): Map<String, Retailer> = service.findRetailers()
}