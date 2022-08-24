package nz.shea.discountdetective.api.data

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Embeddable
class StorageStorePricingInformationId : Serializable {
    @Column(name = "retailerId", insertable = false, updatable = false)
    var retailerId: String? = null

    @Column(name = "productId", insertable = false, updatable = false)
    var productId: String? = null

    @Column(name = "storeId", insertable = false, updatable = false)
    var storeId: String? = null
}