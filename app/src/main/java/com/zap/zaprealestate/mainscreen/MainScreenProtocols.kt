package com.zap.zaprealestate.mainscreen

import com.zap.zaprealestate.model.Property

interface MainScreenProtocols {
    interface View {
        fun showProperties(properties: List<Property>)
        fun showEmptyList()
    }

    interface Presenter {
        fun getPropertiesList()
    }
}