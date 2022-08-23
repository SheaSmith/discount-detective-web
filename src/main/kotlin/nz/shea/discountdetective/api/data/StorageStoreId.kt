package nz.shea.discountdetective.api.data

import java.io.Serializable
import javax.persistence.*

@Embeddable
class StorageStoreId : Serializable {
    @Column(name = "retailerId", nullable = false, insertable = false, updatable = false)
    var retailerId: String? = null

    /**
     * The unique ID for this store.
     *
     * Required.
     */
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    var id: String? = null
}