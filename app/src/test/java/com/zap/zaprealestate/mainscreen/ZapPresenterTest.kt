package com.zap.zaprealestate.mainscreen

import com.zap.zaprealestate.PropertiesScreenProtocols
import com.zap.zaprealestate.mainscreen.zapScreen.ZapScreenPresenter
import com.zap.zaprealestate.model.BusinessType
import com.zap.zaprealestate.model.Property
import com.zap.zaprealestate.model.PropertyRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class ZapPresenterTest {
    @MockK
    private lateinit var propertiesScreenView: PropertiesScreenProtocols.View

    @MockK
    private lateinit var propertyRepository: PropertyRepository

    private lateinit var zapScreenPresenter: ZapScreenPresenter

    private val limitOffSet = 20

    private val minLongitude = -46.693419
    private val maxLongitude = -46.641146

    private val minLatitude=  -23.568704
    private val maxLatitude = -23.546686

    private val salePropertyA = Property(
        "aId",
        listOf("aImage"),
        latitude = minLatitude,
        longitude = minLongitude,
        usableAreas = 100,
        price = "350001",
        businessType = BusinessType.SALE
    )
    private val salePropertyB = Property(
        "bId",
        listOf("bImage"),
        latitude = minLatitude,
        longitude = minLongitude,
        usableAreas = 50,
        price = "3150001",
        businessType = BusinessType.SALE
    )
    private val salePropertyC = Property(
        "bcId",
        listOf("cImage"),
        latitude = minLatitude,
        longitude = minLongitude,
        usableAreas = 200,
        price = "123213213123",
        businessType = BusinessType.SALE
    )

    private val rentPropertyA = Property(
        "aId",
        listOf("aImage"),
        latitude = minLatitude,
        longitude = minLongitude,
        usableAreas = 212,
        price = "12331",
        businessType = BusinessType.RENT
    )

    private val rentPropertyB = Property(
        "bId",
        listOf("bImage"),
        latitude = minLatitude,
        longitude = minLongitude,
        usableAreas = 213,
        price = "123",
        businessType = BusinessType.RENT
    )

    private val rentPropertyC = Property(
        "cId",
        listOf("cImage"),
        latitude = minLatitude,
        longitude = minLongitude,
        usableAreas = 21123123,
        price = "315231231",
        businessType = BusinessType.RENT
    )
    private val pageTwoExpectedProperties =
        listOf(salePropertyA, salePropertyB, salePropertyC, rentPropertyB, rentPropertyB)

    private val pageZeroExpectedProperties =
        listOf(salePropertyA, salePropertyB, salePropertyC, rentPropertyA)


    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        zapScreenPresenter = ZapScreenPresenter(propertiesScreenView, propertyRepository)
    }

    @Test
    fun `should list all ZAP properties`() {
        assertInitialPropertiesList()
    }

    @Test
    fun `should show empty property list when there is no properties`() {

        val expectedProperties = emptyList<Property>()

        returnFromRepository(0, limitOffSet, expectedProperties)

        zapScreenPresenter.getPropertiesList()

        verify { propertiesScreenView.showEmptyList() }
    }

    @Test
    fun `should show error message when there was an error when loading properties`() {
        returnFromRepositoryWithError()

        zapScreenPresenter.getPropertiesList()

        verify { propertiesScreenView.showErrorMessage() }
    }


    @Test
    fun `should show next properties page`() {
        assertInitialPropertiesList()

        assertPageOneProperties()
    }

    @Test
    fun `should show loading on view when fetching properties`() {
        returnFromRepository(0, limitOffSet, pageZeroExpectedProperties)

        zapScreenPresenter.getPropertiesList()

        verify(exactly = 1) { propertiesScreenView.showLoading() }
    }

    @Test
    fun `should load cached properties`(){
        returnFromRepository(0, limitOffSet, pageZeroExpectedProperties, false)

        zapScreenPresenter.getPropertiesList(forceRefresh = false)

        verify { propertiesScreenView.showProperties(eq(pageZeroExpectedProperties)) }
    }

    @Test
    fun `should hide loading on view when fetching properties`() {
        returnFromRepository(0, limitOffSet, pageZeroExpectedProperties)

        zapScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { propertiesScreenView.hideLoading() }
    }

    @Test
    fun `should hide loading on view when was an error`() {
        returnFromRepositoryWithError()

        zapScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { propertiesScreenView.hideLoading() }
    }


    @Test
    fun `should not show properties without latitude and longitude`() {
        val expectedProperties = listOf(salePropertyA, salePropertyC)

        val propertiesWithoutLatAndLong =
            listOf(
                salePropertyA,
                Property(
                    "bId",
                    listOf("bImage"),
                    price = "123123123213",
                    usableAreas = 50,
                    businessType = BusinessType.SALE
                ),
                salePropertyC
            )

        returnFromRepository(0, limitOffSet, propertiesWithoutLatAndLong)

        zapScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { propertiesScreenView.showProperties(eq(expectedProperties)) }
    }

    @Test
    fun `should not show properties with latitude less than minimum`() {
        val expectedProperties = listOf(salePropertyA, salePropertyC)

        val properties =
            listOf(
                salePropertyA,
                Property(
                    "bId",
                    listOf("bImage"),
                    latitude = minLatitude - 1.0,
                    longitude = minLongitude,
                    price = "123123123213",
                    usableAreas = 50,
                    businessType = BusinessType.SALE
                ),
                salePropertyC
            )

        returnFromRepository(0, limitOffSet, properties)

        zapScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { propertiesScreenView.showProperties(eq(expectedProperties)) }
    }

    @Test
    fun `should not show properties with latitude greater than max`() {
        val expectedProperties = listOf(salePropertyA, salePropertyC)

        val properties =
            listOf(
                salePropertyA,
                Property(
                    "bId",
                    listOf("bImage"),
                    latitude = maxLatitude + 1,
                    longitude = minLongitude,
                    price = "123123123213",
                    usableAreas = 50,
                    businessType = BusinessType.SALE
                ),
                salePropertyC
            )

        returnFromRepository(0, limitOffSet, properties)

        zapScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { propertiesScreenView.showProperties(eq(expectedProperties)) }
    }

    @Test
    fun `should not show properties with longitude less than minimum`() {
        val expectedProperties = listOf(salePropertyA, salePropertyC)

        val properties =
            listOf(
                salePropertyA,
                Property(
                    "bId",
                    listOf("bImage"),
                    latitude = minLatitude,
                    longitude = minLongitude - 1,
                    price = "123123123213",
                    usableAreas = 50,
                    businessType = BusinessType.SALE
                ),
                salePropertyC
            )

        returnFromRepository(0, limitOffSet, properties)

        zapScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { propertiesScreenView.showProperties(eq(expectedProperties)) }
    }

    @Test
    fun `should not show properties with longitude greater than max`() {
        val expectedProperties = listOf(salePropertyA, salePropertyC)

        val properties =
            listOf(
                salePropertyA,
                Property(
                    "bId",
                    listOf("bImage"),
                    latitude = minLatitude,
                    longitude = maxLongitude + 1,
                    price = "123123123213",
                    usableAreas = 50,
                    businessType = BusinessType.SALE
                ),
                salePropertyC
            )

        returnFromRepository(0, limitOffSet, properties)

        zapScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { propertiesScreenView.showProperties(eq(expectedProperties)) }
    }

    @Test
    fun `should not show ZAP properties for sale when usable area is zero`() {
        val expectedProperties = listOf(salePropertyA, salePropertyC)

        val propertiesListWithOnePropertyWithZeroUsableAre =
            listOf(
                salePropertyA,
                Property(
                    "bId",
                    listOf("bImage"),
                    latitude = minLatitude,
                    longitude = minLongitude,
                    price = "123123213",
                    businessType = BusinessType.SALE
                ),
                salePropertyC
            )

        returnFromRepository(0, limitOffSet, propertiesListWithOnePropertyWithZeroUsableAre)

        zapScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { propertiesScreenView.showProperties(eq(expectedProperties)) }
    }

    @Test
    fun `should not show ZAP properties for sale when it is for sale and price is bellow 3 500 reais`() {
        val expectedProperties = listOf(salePropertyA, salePropertyC)

        val propertiesListWithOnePropertyWithPriceBellow3500reais =
            listOf(
                salePropertyA,
                Property(
                    "bId",
                    listOf("bImage"),
                    latitude = minLongitude,
                    longitude = minLongitude,
                    usableAreas = 213,
                    price = "34999",
                    businessType = BusinessType.SALE
                ),
                salePropertyC
            )

        returnFromRepository(0, limitOffSet, propertiesListWithOnePropertyWithPriceBellow3500reais)

        zapScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { propertiesScreenView.showProperties(eq(expectedProperties)) }
    }

    @Test
    fun `should not show ZAP property when it is for sale inside bounding box and price less than 3 150 reais`() {
        val expectedProperties = listOf(salePropertyA, salePropertyC)

        val propertiesListWithOnePropertyWithPriceBellow3500reais =
            listOf(
                salePropertyA,
                Property(
                    "bId",
                    listOf("bImage"),
                    latitude = minLatitude,
                    longitude = minLongitude,
                    usableAreas = 213,
                    businessType = BusinessType.SALE,
                    price = "3149"
                ),
                salePropertyC
            )

        returnFromRepository(0, limitOffSet, propertiesListWithOnePropertyWithPriceBellow3500reais)

        zapScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { propertiesScreenView.showProperties(eq(expectedProperties)) }
    }

    @Test
    fun `should show ZAP property when it is for sale, inside Bounding Box and price is 10% cheaper, between 3 150 and 3 500 reais`() {
        val property = Property(
            "bId",
            listOf("bImage"),
            latitude = minLatitude,
            longitude = minLongitude,
            usableAreas = 213,
            businessType = BusinessType.SALE,
            price = "315001"
        )
        val expectedProperties = listOf(salePropertyA, property, salePropertyC)

        returnFromRepository(0, limitOffSet, expectedProperties)

        zapScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { propertiesScreenView.showProperties(eq(expectedProperties)) }
    }


    @Test
    fun `should open property detail screen when it is selected`() {
        val property = Property(
            "bId",
            listOf("bImage"),
            latitude = minLatitude,
            longitude = minLongitude,
            usableAreas = 213,
            businessType = BusinessType.SALE,
            price = "315001"
        )

        zapScreenPresenter.onPropertySelected(property)

        verify(atLeast = 1) { propertiesScreenView.showPropertyDetail(eq(property)) }
    }


    private fun assertPageOneProperties() {
        returnFromRepository(20, limitOffSet, pageTwoExpectedProperties, forceRefresh = false)

        zapScreenPresenter.loadNextPropertiesOffset()

        verify(atLeast = 1) { propertiesScreenView.showProperties(eq(pageZeroExpectedProperties + pageTwoExpectedProperties)) }
    }

    private fun assertInitialPropertiesList() {
        returnFromRepository(0, limitOffSet, pageZeroExpectedProperties)

        zapScreenPresenter.getPropertiesList(forceRefresh = true)

        verify { propertiesScreenView.showProperties(eq(pageZeroExpectedProperties)) }
    }

    private fun returnFromRepository(offSet: Int, limit: Int, expectedProperties: List<Property>, forceRefresh :Boolean = true) {
        slot<((List<Property>) -> Unit)>().let { callback ->
            every {
                propertyRepository.getProperties(
                    offSet = offSet,
                    limit = limit,
                    onError = any(),
                    onSuccess = capture(callback),
                    forceRefresh
                )
            } answers {
                callback.captured.invoke(expectedProperties)
            }
        }
    }

    private fun returnFromRepositoryWithError() {
        slot<(() -> Unit)>().let { callback ->
            every {
                propertyRepository.getProperties(
                    offSet = 0,
                    limit = limitOffSet,
                    onError = capture(callback),
                    onSuccess = any()
                )
            } answers {
                callback.captured.invoke()
            }
        }
    }
}