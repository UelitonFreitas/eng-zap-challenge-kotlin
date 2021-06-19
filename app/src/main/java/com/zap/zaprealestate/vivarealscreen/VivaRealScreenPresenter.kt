package com.zap.zaprealestate.vivarealscreen

import com.zap.zaprealestate.PropertiesScreenProtocols
import com.zap.zaprealestate.mainscreen.isPropertyInExpectedRange
import com.zap.zaprealestate.mainscreen.thereIsLocation
import com.zap.zaprealestate.model.BusinessType
import com.zap.zaprealestate.model.Property
import com.zap.zaprealestate.model.PropertyRepository

class VivaRealScreenPresenter(
    private val view: PropertiesScreenProtocols.View,
    private val propertyRepository: PropertyRepository
) :
    PropertiesScreenProtocols.Presenter {

    private var actualOffSet = 0
    private val limitOffSet = 20
    private val properties = mutableListOf<Property>()

    private fun showProperties(properties: List<Property>) {
        view.hideLoading()

        with(this.properties) {
            addAll(properties.filter(::salePropertyRestrictions))
            takeIf { it.isNotEmpty() }?.let {
                view.showProperties(this)
            } ?: view.showEmptyList()
        }
    }

    private fun salePropertyRestrictions(property: Property) =
        property.thereIsLocation() && hasNumericMonthlyCondoFee(property)

    private fun hasNumericMonthlyCondoFee(property: Property) = try {
        when (property.businessType) {
            BusinessType.RENT -> {
                property.monthlyCondoFee.toLong() < getMaximumMonthlyCondoFee(property)
            }
            else -> true
        }
    } catch (e: Throwable) {
        false
    }

    private fun getMaximumMonthlyCondoFee(property: Property) = run {
        property.rentalTotalPrice.toLong() * when {
            property.isPropertyInExpectedRange() -> 0.5
            else -> 0.3
        }
    }

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

    override fun onPropertySelected(property: Property) {
        view.showPropertyDetail(property)
    }

    private fun showErrorMessage() {
        view.hideLoading()
        view.showErrorMessage()
    }
}