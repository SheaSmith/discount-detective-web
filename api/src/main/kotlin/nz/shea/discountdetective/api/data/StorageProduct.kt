package nz.shea.discountdetective.api.data

import com.example.cosc345.shared.models.Product
import org.hibernate.search.mapper.pojo.automaticindexing.ReindexOnUpdate
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexedEmbedded
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.IndexingDependency
import javax.persistence.*

@Entity
@Indexed
class StorageProduct() {
    @Id
    @Column(nullable = false)
    var id: String? = null

    @JoinColumn(name = "productId", referencedColumnName = "id", updatable = false, insertable = false)
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @IndexedEmbedded
    @IndexingDependency(reindexOnUpdate = ReindexOnUpdate.SHALLOW)
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