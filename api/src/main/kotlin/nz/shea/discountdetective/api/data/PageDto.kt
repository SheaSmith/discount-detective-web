package nz.shea.discountdetective.api.data

class PageDTO<T>(
    val content: T,
    val total: Long
)