package nz.shea.discountdetective.api.data

import com.example.cosc345.shared.models.Product
import com.example.cosc345.shared.models.RetailerProductInformation
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.AssociationInverseSide
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency
import javax.persistence.*

@Entity
class StorageRetailerProductInformation() {

    @EmbeddedId
    var id: StorageRetailerProductInformationId? = null

    var productId: String? = null

    @Column(nullable = false)
    @FullTextField
    var name: String? = null

    @FullTextField
    var brandName: String? = null

    @FullTextField
    var variant: String? = null

    @Column(nullable = false)
    var saleType: String? = null

    @FullTextField
    private var quantity: String? = null
    private var weight: Int? = null

    @ElementCollection
    var barcodes: List<String>? = null

    private var image: String? = null

    @Column(nullable = false)
    var automated: Boolean? = null

    @Column(nullable = false)
    var verified: Boolean? = null

    @JoinColumn(name = "productId", referencedColumnName = "id", updatable = false, insertable = false)
    @JoinColumn(name = "retailerId", referencedColumnName = "retailerId", updatable = false, insertable = false)
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var pricing: List<StorageStorePricingInformation>? = null
    
    constructor(productInfo: RetailerProductInformation, product: StorageProduct) : this() {
        productId = product.id
        id!!.id = productInfo.id!!
        id!!.retailerId = productInfo.retailer!!
        name = productInfo.name!!
        brandName = productInfo.brandName
        variant = productInfo.variant
        saleType = productInfo.saleType!!
        quantity = productInfo.quantity
        weight = productInfo.weight
        barcodes = productInfo.barcodes
        image = productInfo.image
        automated = productInfo.automated!!
        verified = productInfo.verified!!
        pricing = productInfo.pricing!!.map { StorageStorePricingInformation(this, it) }
    }

    init {
        id = StorageRetailerProductInformationId()
    }

    fun toRetailerProductInformation(): RetailerProductInformation {
        return RetailerProductInformation(
            id?.retailerId,
            id?.id,
            name,
            brandName,
            variant,
            saleType,
            quantity,
            weight,
            barcodes,
            image,
            pricing?.map { it.toStorePricingInformation() }?.toMutableList(),
            automated,
            verified
        )
    }
}