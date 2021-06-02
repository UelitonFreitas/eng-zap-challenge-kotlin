package com.zap.zaprealestate.mainscreen

import com.zap.zaprealestate.model.Property
import com.zap.zaprealestate.model.PropertyRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class MainsScreenPresenterTest {
    @MockK
    private lateinit var mainScreenView: MainScreenProtocols.View

    @MockK
    private lateinit var propertyRepository: PropertyRepository

    private lateinit var mainScreenPresenter: MainScreenPresenter

    private val limitOffSet = 20

    private val propertyA = Property("aId", listOf("aImage"), latitude = 1.0, longitude = 1.0)
    private val propertyB = Property("bId", listOf("bImage"), latitude = 1.2, longitude = 1.2)
    private val propertyC = Property("bcId", listOf("cImage"), latitude =1.3, longitude = 1.3)

    private val pageTwoExpectedProperties = listOf(propertyA, propertyB, propertyC)

    private val pageZeroExpectedProperties =
        listOf(propertyA, propertyB, propertyC)

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        mainScreenPresenter = MainScreenPresenter(mainScreenView, propertyRepository)
    }

    @Test
    fun `should list all properties`() {
        assertInitialPropertiesList()
    }

    @Test
    fun `should show empty property list when there is no properties`() {

        val expectedProperties = emptyList<Property>()

        returnFromRepository(0, limitOffSet, expectedProperties)

        mainScreenPresenter.getPropertiesList()

        verify { mainScreenView.showEmptyList() }
    }

    @Test
    fun `should show error message when there was an error when loading properties`() {
        returnFromRepositoryWithError()

        mainScreenPresenter.getPropertiesList()

        verify { mainScreenView.showErrorMessage() }
    }


    @Test
    fun `should show next properties page`(){
        assertInitialPropertiesList()

        assertPageOneProperties()
    }

    @Test
    fun `should show loading on view when fetching properties`(){
        returnFromRepository(0, limitOffSet, pageZeroExpectedProperties)

        mainScreenPresenter.getPropertiesList()

        verify(exactly = 1) { mainScreenView.showLoading() }
    }

    @Test
    fun `should hide loading on view when fetching properties`(){
        returnFromRepository(0, limitOffSet, pageZeroExpectedProperties)

        mainScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { mainScreenView.hideLoading() }
    }

    @Test
    fun `should hide loading on view when was an error`(){
        returnFromRepositoryWithError()

        mainScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { mainScreenView.hideLoading() }
    }


    @Test
    fun `should not show properties without latitude and longitude`(){
        val expectedProperties = listOf(propertyA, propertyC)

        val propertiesWithoutLatAndLong =
            listOf(propertyA, Property("bId", listOf("bImage")), propertyC)

        returnFromRepository(0, limitOffSet, propertiesWithoutLatAndLong)

        mainScreenPresenter.getPropertiesList()

        verify(atLeast = 1) { mainScreenView.showProperties(eq(expectedProperties)) }
    }

    private fun assertPageOneProperties() {
        returnFromRepository(20, limitOffSet, pageTwoExpectedProperties)

        mainScreenPresenter.loadNextPropertiesOffset()

        verify(atLeast = 1) { mainScreenView.showProperties(eq(pageZeroExpectedProperties + pageTwoExpectedProperties)) }
    }

    private fun assertInitialPropertiesList() {
        returnFromRepository(0, limitOffSet, pageZeroExpectedProperties)

        mainScreenPresenter.getPropertiesList()

        verify { mainScreenView.showProperties(eq(pageZeroExpectedProperties)) }
    }

    private fun returnFromRepository(offSet: Int, limit: Int, expectedProperties: List<Property>) {
        slot<((List<Property>) -> Unit)>().let { callback ->
            every {
                propertyRepository.getProperties (
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
                propertyRepository.getProperties (
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