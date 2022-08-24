package nz.shea.discountdetective.api.data

import com.example.cosc345.shared.models.Store
import javax.persistence.*

@Entity
class StorageStore() {
    @EmbeddedId
    var storeId: StorageStoreId? = null

    /**
     * The name of the store, for example, Centre City.
     *
     * Required.
     */
    @Column(nullable = false)
    var name: String? = null

    /**
     * The address of the store.
     *
     * Required.
     */
    @Column(nullable = true)
    var address: String? = null

    /**
     * The latitude of the store.
     *
     * Required.
     */
    @Column(nullable = true)
    var latitude: Double? = null

    /**
     * The longitude of the store.
     *
     * Required.
     */
    @Column(nullable = true)
    var longitude: Double? = null

    /**
     * Whether the store was automatically added by the scraper, rather than by the retailer themselves, or a user.
     *
     * Required.
     */
    @Column(nullable = false)
    var automated: Boolean? = null

    constructor(store: Store, retailer: StorageRetailer) : this() {
        storeId!!.id = store.id
        storeId!!.retailerId = retailer.id
        name = store.name!!
        address = store.address
        latitude = store.latitude
        longitude = store.longitude
        automated = store.automated!!
    }

    init {
        this.storeId = StorageStoreId()
    }

    fun toStore(): Store {
        return Store(
            storeId?.id,
            name,
            address,
            latitude,
            longitude,
            automated
        )
    }
}