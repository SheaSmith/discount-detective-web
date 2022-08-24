package nz.shea.discountdetective.api.data

import com.example.cosc345.shared.models.Product
import javax.persistence.*

@Entity
class StorageProduct() {
    @Id
    @Column(nullable = false)
    var id: String? = null

    @JoinColumn(name = "productId", referencedColumnName = "id")
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var information: List<StorageRetailerProductInformation>? = null

    constructor(product: Product, id: String): this() {
        this.id = id
        information = product.information!!.map { StorageRetailerProductInformation(it, this) }
    }

    fun toProduct(): Pair<String, Product> {
        val product = Product(information?.map { it.toRetailerProductInformation() }?.toMutableList())
        return Pair(id!!, product)
    }
}