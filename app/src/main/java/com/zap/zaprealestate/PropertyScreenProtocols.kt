package com.zap.zaprealestate

import com.zap.zaprealestate.model.Property

interface PropertyScreenProtocols {
    interface View {
        fun showProperties(properties: List<Property>)
        fun showEmptyList()
        fun showErrorMessage()
        fun showLoading()
        fun hideLoading()
    }

    interface Presenter {
        fun getPropertiesList()
        fun loadNextPropertiesOffset()
    }
}