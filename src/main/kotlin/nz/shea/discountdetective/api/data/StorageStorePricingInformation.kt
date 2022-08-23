package nz.shea.discountdetective.api.data

import com.example.cosc345.shared.models.RetailerProductInformation
import com.example.cosc345.shared.models.StorePricingInformation
import javax.persistence.*

@Entity
class StorageStorePricingInformation() {
    @EmbeddedId
    var pricingId: StorageStorePricingInformationId? = null

    var price: Int? = null
    var discountPrice: Int? = null
    var multiBuyQuantity: Int? = null
    var multiBuyPrice: Int? = null
    var clubOnly: Boolean? = null
    var automated: Boolean? = null
    var verified: Boolean? = null
    
    constructor(
        retailerProductInfo: StorageRetailerProductInformation,
        storePricingInformation: StorePricingInformation
    ): this() {
        pricingId!!.productId = retailerProductInfo.id!!.id
        pricingId!!.retailerId = retailerProductInfo.id!!.retailerId
        pricingId!!.storeId = storePricingInformation.store
        price = storePricingInformation.price
        discountPrice = storePricingInformation.discountPrice
        multiBuyQuantity = storePricingInformation.multiBuyQuantity
        multiBuyPrice = storePricingInformation.multiBuyPrice
        clubOnly = storePricingInformation.clubOnly
        automated = storePricingInformation.automated
        verified = storePricingInformation.verified
    }

    init {
        pricingId = StorageStorePricingInformationId()
    }

    fun toStorePricingInformation(): StorePricingInformation =
        StorePricingInformation(
            pricingId?.storeId,
            price,
            discountPrice,
            multiBuyQuantity,
            multiBuyPrice,
            clubOnly,
            automated,
            verified
        )
}
