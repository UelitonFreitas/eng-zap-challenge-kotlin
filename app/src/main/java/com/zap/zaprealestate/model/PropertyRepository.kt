package com.zap.zaprealestate.model

interface PropertyRepository {
    fun getProperties(offSet: Int, limit: Int, onError: (() -> Unit)? = null, onSuccess: (List<Property>) -> Unit, forceRefresh :Boolean = true)
}