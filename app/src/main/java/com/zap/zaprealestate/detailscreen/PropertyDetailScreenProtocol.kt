package com.zap.zaprealestate.detailscreen

import com.zap.zaprealestate.model.Property

interface PropertyDetailScreenProtocol {
    interface View {
        fun showProperty(property: Property)
        fun showPropertyNotFoundErrorMessage()
    }

    interface Presenter {
        fun loadProperty()
    }
}