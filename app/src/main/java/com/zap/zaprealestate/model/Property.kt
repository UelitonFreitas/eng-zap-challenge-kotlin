package com.zap.zaprealestate.model

data class Property(val id : String,
                    val images: List<String>,
                    val latitude: Double = 0.0,
                    val longitude: Double = 0.0,
                    val usableAreas: Long = 0,
                    val price: String = "",
                    val businessType: BusinessType,
                    val monthlyCondoFee: String = "",
                    val rentalTotalPrice: String = "",
                    val bathrooms: Int = 0,
                    val bedrooms: Int = 0
)

enum class BusinessType{
    SALE, RENT
}