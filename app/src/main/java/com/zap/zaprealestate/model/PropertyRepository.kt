package com.zap.zaprealestate.model

interface PropertyRepository {
    fun getProperties(onError: (() -> Unit)? = null, onSuccess: (List<Property>) -> Unit)
}