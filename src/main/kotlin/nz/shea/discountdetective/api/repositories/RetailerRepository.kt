package nz.shea.discountdetective.api.repositories

import nz.shea.discountdetective.api.data.StorageRetailer
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface RetailerRepository : CrudRepository<StorageRetailer, String>