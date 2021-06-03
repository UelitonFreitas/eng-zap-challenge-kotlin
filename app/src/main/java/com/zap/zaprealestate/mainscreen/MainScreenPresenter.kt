package com.zap.zaprealestate.mainscreen

import com.zap.zaprealestate.model.BusinessType
import com.zap.zaprealestate.model.Property
import com.zap.zaprealestate.model.PropertyRepository

class MainScreenPresenter(
    private val view: MainScreenProtocols.View,
    private val propertyRepository: PropertyRepository
) :
    MainScreenProtocols.Presenter {

    private var actualOffSet = 0
    private val limitOffSet = 20
    private val properties = mutableListOf<Property>()

    private fun showProperties(properties: List<Property>) {
        view.hideLoading()

        with(this.properties) {
            addAll(
                properties
                    .filter(::thereIsLocation)
                    .filter(::salePropertyRestrictions)
            )
            takeIf { it.isNotEmpty() }?.let {
                view.showProperties(this)
            } ?: view.showEmptyList()
        }
    }

    private fun salePropertyRestrictions(property: Property) =
        hasUsableAreaMinimumPrice(property) && isPropertyInExpectedRange(property)

    private fun hasUsableAreaMinimumPrice(property: Property) = try {
            when (property.businessType) {
                BusinessType.SALE -> thereIsUsableAre(property) && property.price.toLong() > 350000
                else -> true
            }
        } catch (e: Throwable) {
            false
        }

    private fun thereIsUsableAre(it: Property) = it.usableAreas != 0L

    private fun isPropertyInExpectedRange(property: Property): Boolean {

        val minLongitude =  -46.693419
        val maxLongitude = -46.641146

        val minLatitude=  -23.568704
        val maxLatitude = -23.546686

        val isInRange = property.run {
            latitude in minLatitude..maxLatitude && longitude in minLongitude..maxLongitude
        }

        return thereIsLocation(property) && isInRange
    }

    private fun thereIsLocation(property: Property) =
        property.latitude != 0.0 && property.longitude != 0.0

    override fun getPropertiesList() {
        view.showLoading()
        propertyRepository.getProperties(
            actualOffSet,
            limit = limitOffSet,
            onError = ::showErrorMessage,
            onSuccess = ::showProperties
        )
    }

    override fun loadNextPropertiesOffset() {
        actualOffSet += limitOffSet
        getPropertiesList()
    }

    private fun showErrorMessage() {
        view.hideLoading()
        view.showErrorMessage()
    }
}