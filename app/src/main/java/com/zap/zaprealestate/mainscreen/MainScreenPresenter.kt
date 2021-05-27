package com.zap.zaprealestate.mainscreen

import com.zap.zaprealestate.model.Property
import com.zap.zaprealestate.model.PropertyRepository

class MainScreenPresenter (
    private val view: MainScreenProtocols.View,
    private val propertyRepository: PropertyRepository
) :
    MainScreenProtocols.Presenter {

    private fun showProperties(properties: List<Property>) {

        properties.takeIf { it.isNotEmpty() }?.let {
            view.showProperties(properties)
        } ?: view.showEmptyList()
    }

    override fun getPropertiesList() {
        propertyRepository.getProperties(onSuccess = ::showProperties)
    }
}