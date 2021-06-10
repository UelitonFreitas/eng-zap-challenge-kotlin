package com.zap.zaprealestate.mainscreen

import com.zap.zaprealestate.model.BusinessType
import com.zap.zaprealestate.model.Property
import com.zap.zaprealestate.model.PropertyRepository

class ZapScreenPresenter(
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
                    .filter(::salePropertyRestrictions)
            )
            takeIf { it.isNotEmpty() }?.let {
                view.showProperties(this)
            } ?: view.showEmptyList()
        }
    }

    private fun salePropertyRestrictions(property: Property) =
        hasUsableAreaMinimumPrice(property) && property.isPropertyInExpectedRange()

    private fun hasUsableAreaMinimumPrice(property: Property) = try {
            when (property.businessType) {
                BusinessType.SALE -> thereIsUsableAre(property) && (property.price.toLong() > calculateSalePropertyPrice(property))
                else -> true
            }
        } catch (e: Throwable) {
            false
        }

    private fun calculateSalePropertyPrice(property: Property): Long {
        val minimumPrice = 350000L
        return when {
            property.businessType == BusinessType.SALE && property.isPropertyInExpectedRange() -> minimumPrice - (minimumPrice * 0.1).toLong()
            else -> minimumPrice
        }
    }

    private fun thereIsUsableAre(it: Property) = it.usableAreas != 0L


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