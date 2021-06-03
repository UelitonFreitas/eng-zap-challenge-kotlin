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
                    .filter(::thereIsUsableAre)
                    .filter(::shouldCostAtLast350000ReaisForSaleProperty)
            )
            takeIf { it.isNotEmpty() }?.let {
                view.showProperties(this)
            } ?: view.showEmptyList()
        }
    }

    private fun shouldCostAtLast350000ReaisForSaleProperty(it: Property) = try {
        when (it.businessType) {
            BusinessType.SALE -> it.price.toLong() > 350000
            else -> true
        }
    } catch (e: Throwable) {
        false
    }

    private fun thereIsUsableAre(it: Property) = it.usableAreas != 0L

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