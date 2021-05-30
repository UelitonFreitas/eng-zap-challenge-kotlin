package com.zap.zaprealestate.model

class PropertiesRepositoryImpl: PropertyRepository {
    override fun getProperties(onError: (() -> Unit)?, onSuccess: (List<Property>) -> Unit) {
        onSuccess(listOf(Property("aId"), Property("bId"), Property("cId")))
    }
}