package com.zap.zaprealestate.mainscreen

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

class VivaRealPresenterTest {
    @MockK
    private lateinit var mainScreenView: MainScreenProtocols.View

    @MockK
    private lateinit var propertyRepository: PropertyRepository

    private lateinit var vivaRealScreenPresenter: VivaRealScreenPresenter

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
        businessType = BusinessType.RENT,
        monthlyCondoFee = "600",
        rentalTotalPrice = "20000"
    )

    private val rentPropertyB = Property(
        "bId",
        listOf("bImage"),
        latitude = minLatitude,
        longitude = minLongitude,
        usableAreas = 213,
        price = "123",
        businessType = BusinessType.RENT,
        monthlyCondoFee = "600",
        rentalTotalPrice = "20000"
    )

    private val rentPropertyC = Property(
        "cId",
        listOf("cImage"),
        latitude = minLatitude,
        longitude = minLongitude,
        usableAreas = 21123123,
        price = "315231231",
        businessType = BusinessType.RENT,
        monthlyCondoFee = "600",
        rentalTotalPrice = "20000"
    )
    private val pageTwoExpectedProperties =
        listOf(salePropertyA, salePropertyB, salePropertyC, rentPropertyB, rentPropertyB)

    private val pageZeroExpectedProperties =
        listOf(salePropertyA, salePropertyB, salePropertyC, rentPropertyA)


    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        vivaRealScreenPresenter = VivaRealScreenPresenter(mainScreenView, propertyRepository)
    }

    @Test
    fun `should list all Viva Real properties`() {
        assertInitialPropertiesList()
    }

    @Test
    fun `should show empty property list when there is no properties`() {

        val expectedProperties = emptyList<Property>()

        returnFromRepository(0, limitOffSet, expectedProperties)

        vivaRealScreenPresenter.getPropertiesList()

        verify { mainScreenView.showEmptyList() }
    }

    @Test
    fun `should show error message when there was an error when loading properties`() {
        returnFromRepositoryWithError()

        vivaRealScreenPresenter.getPropertiesList()

        verify { mainScreenView.showErrorMessage() }
    }


    @Test
    fun `should show next properties page`() {
        assertInitialPropertiesList()

        assertPageOneProperties()
    }

    @Test
    fun `should show loading on view when fetching properties`() {
        returnFromRepository(0, limitOffSet, pageZeroExpectedProperties)

        vivaRealScreenPresenter.getPropertiesList()

        verify(exactly = 1) { mainScreenView.showLoading() }
    }

    @Test
    fun `should hide loading on view when fetching properties`() {
        returnFromRepository(0, limitOffSet, pageZeroExpectedProperties)

        vivaRealScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { mainScreenView.hideLoading() }
    }

    @Test
    fun `should hide loading on view when was an error`() {
        returnFromRepositoryWithError()

        vivaRealScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { mainScreenView.hideLoading() }
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

        vivaRealScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { mainScreenView.showProperties(eq(expectedProperties)) }
    }

    @Test
    fun `should not show rent Viva Real properties with invalid monthly condo fee`() {
        val expectedProperties = listOf(rentPropertyA, rentPropertyC)

        val propertiesWithEmptyMonthlyFee =
            listOf(
                rentPropertyA,
                Property(
                    "bId",
                    listOf("bImage"),
                    latitude = minLongitude,
                    longitude = minLongitude,
                    usableAreas = 213,
                    price = "34999",
                    businessType = BusinessType.RENT,
                    monthlyCondoFee = ""
                ),
                rentPropertyC
            )

        returnFromRepository(0, limitOffSet, propertiesWithEmptyMonthlyFee)

        vivaRealScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { mainScreenView.showProperties(eq(expectedProperties)) }
    }

    @Test
    fun `should not show rent Viva Real properties with monthly condo fee more than 30 percent of rent`() {
        val expectedProperties = listOf(rentPropertyA, rentPropertyC)

        val propertiesWithEmptyMonthlyFee =
            listOf(
                rentPropertyA,
                Property(
                    "bId",
                    listOf("bImage"),
                    latitude = minLongitude,
                    longitude = minLongitude,
                    usableAreas = 213,
                    price = "34999",
                    businessType = BusinessType.RENT,
                    monthlyCondoFee = "6001",
                    rentalTotalPrice = "20000"
                ),
                rentPropertyC
            )

        returnFromRepository(0, limitOffSet, propertiesWithEmptyMonthlyFee)

        vivaRealScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { mainScreenView.showProperties(eq(expectedProperties)) }
    }
    @Test
    fun `should show rent Viva Real properties with monthly condo fee less or equal than 50 percent of rent when its in bounding box`() {
        val property = Property(
            "bId",
            listOf("bImage"),
            latitude = minLatitude,
            longitude = minLongitude,
            usableAreas = 213,
            price = "34999",
            businessType = BusinessType.RENT,
            monthlyCondoFee = "9999",
            rentalTotalPrice = "20000"
        )

        val expectedProperties = listOf(rentPropertyA, property, rentPropertyC)

        returnFromRepository(0, limitOffSet, expectedProperties)

        vivaRealScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { mainScreenView.showProperties(eq(expectedProperties)) }
    }


    private fun assertPageOneProperties() {
        returnFromRepository(20, limitOffSet, pageTwoExpectedProperties)

        vivaRealScreenPresenter.loadNextPropertiesOffset()

        verify(atLeast = 1) { mainScreenView.showProperties(eq(pageZeroExpectedProperties + pageTwoExpectedProperties)) }
    }

    private fun assertInitialPropertiesList() {
        returnFromRepository(0, limitOffSet, pageZeroExpectedProperties)

        vivaRealScreenPresenter.getPropertiesList()

        verify { mainScreenView.showProperties(eq(pageZeroExpectedProperties)) }
    }

    private fun returnFromRepository(offSet: Int, limit: Int, expectedProperties: List<Property>) {
        slot<((List<Property>) -> Unit)>().let { callback ->
            every {
                propertyRepository.getProperties(
                    offSet = offSet,
                    limit = limit,
                    onError = any(),
                    onSuccess = capture(callback)
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