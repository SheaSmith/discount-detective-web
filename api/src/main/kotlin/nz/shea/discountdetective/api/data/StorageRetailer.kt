package nz.shea.discountdetective.api.data

import com.example.cosc345.shared.models.Retailer
import com.example.cosc345.shared.models.Store
import javax.persistence.*

@Entity
class StorageRetailer() {
    @Id
    @Column(nullable = false)
    var id: String? = null

    @Column(nullable = false)
    var name: String? = null

    @Column(nullable = false)
    var automated: Boolean? = null

    @Column(nullable = false)
    var verified: Boolean? = null

    @Column(nullable = false)
    var colourLight: Long? = null

    @Column(nullable = false)
    var onColourLight: Long? = null

    @Column(nullable = false)
    var onColourDark: Long? = null

    @Column(nullable = false)
    var colourDark: Long? = null

    @Column(nullable = false)
    var initialism: String? = null

    @Column(nullable = false)
    var local: Boolean? = null

    @JoinColumn(name = "retailerId", referencedColumnName = "id", updatable = false, insertable = false)
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var stores: List<StorageStore>? = null

    constructor(retailer: Retailer, id: String) : this() {
        this.id = id
        name = retailer.name!!
        automated = retailer.automated!!
        verified = retailer.verified!!
        colourLight = retailer.colourLight!!
        onColourLight = retailer.onColourLight!!
        colourDark = retailer.colourDark!!
        onColourDark = retailer.onColourDark!!
        initialism = retailer.initialism!!
        local = retailer.local!!
        stores = retailer.stores!!.map { StorageStore(it, this) }
    }

    fun toRetailer(): Pair<String, Retailer> {
        return Pair(
            id!!, Retailer(
                name,
                automated,
                verified,
                stores?.map { it.toStore() },
                colourLight,
                onColourLight,
                colourDark,
                onColourDark,
                initialism,
                local
            )
        )
    }
}
