package com.zap.zaprealestate.mainscreen

import com.zap.zaprealestate.model.Property
import com.zap.zaprealestate.model.PropertyRepository

class MainScreenPresenter (
    private val view: MainScreenProtocols.View,
    private val propertyRepository: PropertyRepository
) :
    MainScreenProtocols.Presenter {

    private var actualOffSet = 0
    private val limitOffSet = 20
    private val properties = mutableListOf<Property>()

    private fun showProperties(properties: List<Property>) {
        view.hideLoading()

        with(this.properties){
            addAll(properties.filter { it.latitude != 0.0 && it.longitude != 0.0 })
            takeIf { it.isNotEmpty() }?.let {
                view.showProperties(this)
            } ?: view.showEmptyList()
        }
    }

    override fun getPropertiesList() {
        view.showLoading()
        propertyRepository.getProperties(
            actualOffSet,
            limit = limitOffSet,
            onError = ::showErrorMessage,
            onSuccess = ::showProperties)
    }

    override fun loadNextPropertiesOffset() {
        actualOffSet += limitOffSet
        getPropertiesList()
    }

    private fun showErrorMessage(){
        view.hideLoading()
        view.showErrorMessage()
    }
}