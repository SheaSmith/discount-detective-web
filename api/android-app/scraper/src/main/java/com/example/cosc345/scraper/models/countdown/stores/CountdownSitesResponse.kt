package com.example.cosc345.scraper.models.countdown.stores

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * The response returned by the Countdown get stores request.
 *
 * @constructor Create a new instance of this object. This should only be used by Moshi.
 */
@JsonClass(generateAdapter = true)
data class CountdownSitesResponse(
    /**
     * The list of different stores.
     */
    @Json(name = "siteDetail")
    val siteDetails: Array<CountdownSiteDetail>
) {
    /**
     * Whether this object equals another, taking into account the array. This is boilerplate suggested by Kotlin.
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CountdownSitesResponse

        if (!siteDetails.contentEquals(other.siteDetails)) return false

        return true
    }

    /**
     * Generates a hashcode for this class, taking into account the array. This is boilerplate suggested by Kotlin.
     */
    override fun hashCode(): Int {
        return siteDetails.contentHashCode()
    }
}
