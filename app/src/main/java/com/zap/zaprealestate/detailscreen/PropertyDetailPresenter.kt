package com.zap.zaprealestate.detailscreen

import com.zap.zaprealestate.model.Property

class PropertyDetailPresenter(
    private val property: Property?,
    private val view: PropertyDetailScreenProtocol.View
) : PropertyDetailScreenProtocol.Presenter {

    override fun loadProperty() {
        property?.let { view.showProperty(it) } ?: view.showPropertyNotFoundErrorMessage()
    }
}