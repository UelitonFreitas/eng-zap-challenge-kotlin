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

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        mainScreenPresenter = MainScreenPresenter(mainScreenView, propertyRepository)
    }

    @Test
    fun `should list all properties`() {
        val expectedProperties =
            listOf(Property("aId", listOf("aImage")), Property("bId", listOf("aImage")), Property("bId", listOf("aImage")))

        returnFromRepository(expectedProperties)

        mainScreenPresenter.getPropertiesList()

        verify { mainScreenView.showProperties(eq(expectedProperties)) }
    }

    @Test
    fun `should show empty property list when there is no properties`() {

        val expectedProperties = emptyList<Property>()

        returnFromRepository(expectedProperties)

        mainScreenPresenter.getPropertiesList()

        verify { mainScreenView.showEmptyList() }
    }

    private fun returnFromRepository(expectedProperties: List<Property>) {
        slot<((List<Property>) -> Unit)>().let { callback ->
            every {
                propertyRepository.getProperties (
                    onError = any(),
                    onSuccess = capture(callback)
                )
            } answers {
                callback.captured.invoke(expectedProperties)
            }
        }
    }
}