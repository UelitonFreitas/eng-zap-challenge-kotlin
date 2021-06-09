package com.zap.zaprealestate.model.remote.models

import com.google.gson.annotations.SerializedName

data class PropertyResponse(
    @SerializedName("id") val id: String,
    @SerializedName("images") val images: List<String>,
    @SerializedName("usableAreas") val usableAreas: Long,
    @SerializedName("address") val address: AddressResponse,
    @SerializedName("pricingInfos") val pricingInfos: PricingInfos
)

data class AddressResponse(
    @SerializedName("city") val city: String,
    @SerializedName("neighborhood") val neighborhood: String,
    @SerializedName("geoLocation") val geoLocation: GeoLocationResponse
)

data class GeoLocationResponse(
    @SerializedName("precision") val precision: String,
    @SerializedName("location") val location: LocationResponse
)

data class LocationResponse(
    @SerializedName("lon") val lon: Double,
    @SerializedName("lat") val lat: Double
)

data class PricingInfos(
    @SerializedName("yearlyIptu") val yearlyIptu: String,
    @SerializedName("price") val price: String,
    @SerializedName("businessType") val businessType: String,
    @SerializedName("monthlyCondoFee") val monthlyCondoFee: String
)
