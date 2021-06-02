package com.zap.zaprealestate.model

data class Property(val id : String, val images: List<String>, val latitude: Double = 0.0, val longitude: Double = 0.0)