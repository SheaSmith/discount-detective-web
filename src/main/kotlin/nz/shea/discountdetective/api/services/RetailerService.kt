package nz.shea.discountdetective.api.services

import com.example.cosc345.shared.models.Retailer
import nz.shea.discountdetective.api.data.StorageRetailer
import nz.shea.discountdetective.api.repositories.RetailerRepository
import org.springframework.stereotype.Service

@Service
class RetailerService(private val db: RetailerRepository) {
    fun findRetailers(): Map<String, Retailer> = db.findAll().associate { it.toRetailer() }

    fun insertAll(retailers: Map<String, Retailer>): List<StorageRetailer> {
        val storageRetailers = retailers.map { StorageRetailer(it.value, it.key) }
        db.saveAll(storageRetailers)
        return storageRetailers
    }
}