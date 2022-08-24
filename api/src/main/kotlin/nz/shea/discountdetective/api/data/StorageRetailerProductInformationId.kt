package nz.shea.discountdetective.api.data

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Embeddable
class StorageRetailerProductInformationId : Serializable {
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    var id: String? = null

    @Column(name = "retailerId", nullable = false, insertable = false, updatable = false)
    var retailerId: String? = null
}