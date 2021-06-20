package com.zap.zaprealestate.mainscreen

import com.zap.zaprealestate.detailscreen.PropertyDetailPresenter
import com.zap.zaprealestate.detailscreen.PropertyDetailScreenProtocol
import com.zap.zaprealestate.model.BusinessType
import com.zap.zaprealestate.model.Property
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class PropertyDetailPresenterTest {

    @MockK
    private lateinit var propertyDetailScreen: PropertyDetailScreenProtocol.View

    private lateinit var propertyDetailPresenter: PropertyDetailPresenter

    private val minLongitude = -46.693419
    private val minLatitude=  -23.568704

    private val salePropertyA = Property(
        "aId",
        listOf("aImage"),
        latitude = minLatitude,
        longitude = minLongitude,
        usableAreas = 100,
        price = "350001",
        businessType = BusinessType.SALE
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        propertyDetailPresenter = PropertyDetailPresenter(salePropertyA, propertyDetailScreen)
    }

    @Test
    fun `should show property details`(){

        propertyDetailPresenter.loadProperty()

        verify(exactly = 1) { propertyDetailScreen.showProperty(eq(salePropertyA)) }
    }

    @Test
    fun `should show property error message when there is no property`(){

        val propertyDetailPresenter = PropertyDetailPresenter(null, propertyDetailScreen)

        propertyDetailPresenter.loadProperty()

        verify(exactly = 1) { propertyDetailScreen.showPropertyNotFoundErrorMessage() }
    }
}