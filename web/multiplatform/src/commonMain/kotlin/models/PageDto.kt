package nz.shea.discountdetective.api.data

@kotlinx.serialization.Serializable
class PageDTO<T>(
    val content: T,
    val total: Long
)