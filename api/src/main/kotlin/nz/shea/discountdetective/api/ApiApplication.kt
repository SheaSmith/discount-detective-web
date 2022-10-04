package nz.shea.discountdetective.api

import com.example.cosc345.shared.models.Product
import com.example.cosc345.shared.models.Retailer
import nz.shea.discountdetective.api.data.PageDTO
import nz.shea.discountdetective.api.services.ProductService
import nz.shea.discountdetective.api.services.RetailerService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.web.bind.annotation.*
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.PushbackInputStream
import java.net.URL
import java.net.URLConnection


@SpringBootApplication
class ApiApplication

fun main(args: Array<String>) {
    runApplication<ApiApplication>(*args)
}

@RestController
@RequestMapping("/products")
@CrossOrigin
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
@CrossOrigin
class RetailerResource(val service: RetailerService) {
    @RequestMapping("/")
    @GetMapping
    fun index(): Map<String, Retailer> = service.findRetailers()
}

@RestController
@RequestMapping("/image-proxy")
@CrossOrigin
class ImageProxyResource {
    @RequestMapping("")
    fun proxyImage(@RequestParam imageUrl: String, requestEntity: RequestEntity<Any>): HttpEntity<ByteArray> {
        val url = URL(imageUrl)
        val pushbackLimit = 100
        val urlStream: InputStream = url.openStream()
        val pushUrlStream = PushbackInputStream(urlStream, pushbackLimit)
        val firstBytes = ByteArray(pushbackLimit)
        pushUrlStream.read(firstBytes)
        pushUrlStream.unread(firstBytes)

        val bais = ByteArrayInputStream(firstBytes)
        val mimeType: String = URLConnection.guessContentTypeFromStream(bais)

        val bytes = pushUrlStream.readAllBytes()

        val headers = HttpHeaders()
        headers.contentLength = bytes.size.toLong()
        headers.contentType = MediaType.parseMediaType(mimeType)

        return HttpEntity<ByteArray>(bytes, headers)
    }
}