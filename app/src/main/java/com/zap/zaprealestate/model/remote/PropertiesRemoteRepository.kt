package com.zap.zaprealestate.model.remote

import android.util.Log
import com.zap.zaprealestate.model.BusinessType
import com.zap.zaprealestate.model.Property
import com.zap.zaprealestate.model.PropertyRepository
import com.zap.zaprealestate.model.remote.models.PropertyResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class PropertiesRepositoryImpl : PropertyRepository {

    companion object {
        var cachedProperties: List<Property>? = null
        const val tag = "PropertiesRepositoryImpl"
    }

    private val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val httpClient = OkHttpClient.Builder().apply {
        addInterceptor(logging)
    }

    private var retrofit = Retrofit.Builder()
        .baseUrl("http://grupozap-code-challenge.s3-website-us-east-1.amazonaws.com/sources/")
        .client(httpClient.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val propertiesApiClient: PropertiesAPI = retrofit.create(PropertiesAPI::class.java)

    private val localScope = CoroutineScope(Dispatchers.IO)

    override fun getProperties(
        offSet: Int,
        limit: Int,
        onError: (() -> Unit)?,
        onSuccess: (List<Property>) -> Unit,
        forceRefresh: Boolean
    ) {

        cachedProperties?.takeIf { forceRefresh.not() }?.let { properties ->

            Log.i(tag, "Lading cached data with offset $offSet and  $limit")

            val (fromIndex, toIndex) = getIndexSlice(offSet, limit)

            Log.i(tag, "Lading cached data from index: $fromIndex to $toIndex")

            when {
                fromIndex >= properties.lastIndex -> {
                    onSuccess(emptyList())
                }
                toIndex > properties.lastIndex -> onSuccess(
                    properties.subList(
                        fromIndex,
                        properties.lastIndex
                    )
                )
                else -> onSuccess(properties.subList(fromIndex, toIndex))
            }

        } ?: loadPropertiesFromAPI(
            offSet,
            limit,
            onError,
            onSuccess
        )
    }

    private fun loadPropertiesFromAPI(
        offSet: Int,
        limit: Int,
        onError: (() -> Unit)?,
        onSuccess: (List<Property>) -> Unit
    ) {

        Log.i(tag, "Lading data from API")

        localScope.launch {
            propertiesApiClient.getProperties()
                ?.enqueue(object : Callback<List<PropertyResponse>?> {
                    override fun onFailure(call: Call<List<PropertyResponse>?>, t: Throwable) {
                        t.printStackTrace()
                        onError?.invoke()
                    }

                    override fun onResponse(
                        call: Call<List<PropertyResponse>?>,
                        response: Response<List<PropertyResponse>?>
                    ) {
                        val propertiesResponse = response.body()

                        val properties =
                            propertiesResponse?.map {
                                it.run {

                                    Property(
                                        id = id,
                                        images = images,
                                        businessType = getBusinessType(pricingInfos.businessType),
                                        latitude = address.geoLocation.location.lat,
                                        longitude = address.geoLocation.location.lon,
                                        usableAreas = usableAreas,
                                        price = pricingInfos.price,
                                        bathrooms = bathrooms,
                                        bedrooms = bedrooms

                                    )
                                }
                            } ?: emptyList()

                        properties.takeIf {
                            it.isNotEmpty()
                        }?.let {
                            cacheProperties(it)

                            val (fromIndex, toIndex) = getIndexSlice(offSet, limit)

                            Log.i(tag, "Lading data from API from indexes: $fromIndex to $toIndex")

                            onSuccess(properties.subList(fromIndex, toIndex))
                        } ?: onError?.invoke()
                    }
                })
        }
    }

    private fun getBusinessType(businessType: String) = when (businessType) {
            "SALE" -> BusinessType.SALE
            else -> BusinessType.RENT
        }

    private fun cacheProperties(it: List<Property>) {
        cachedProperties = it
    }

    private fun getIndexSlice(
        offSet: Int,
        limit: Int
    ): Pair<Int, Int> {
        val fromIndex = offSet
        val toIndex = fromIndex + limit
        return Pair(fromIndex, toIndex)
    }

    fun cancel(){
        localScope.cancel()
    }
}

interface PropertiesAPI {
    @GET("source-1.json")
    fun getProperties(): Call<List<PropertyResponse>?>?
}