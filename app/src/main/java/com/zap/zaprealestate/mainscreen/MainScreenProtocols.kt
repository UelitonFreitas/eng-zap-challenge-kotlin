package com.zap.zaprealestate.mainscreen

import com.zap.zaprealestate.model.Property

interface MainScreenProtocols {
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