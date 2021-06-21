package com.zap.zaprealestate

import com.zap.zaprealestate.model.Property

interface PropertiesScreenProtocols {
    interface View {
        fun showProperties(properties: List<Property>)
        fun showEmptyList()
        fun showErrorMessage()
        fun showLoading()
        fun hideLoading()
        fun showPropertyDetail(property: Property)
    }

    interface Presenter {
        fun getPropertiesList(forceRefresh :Boolean = true)
        fun loadNextPropertiesOffset()
        fun onPropertySelected(property: Property)
    }
}