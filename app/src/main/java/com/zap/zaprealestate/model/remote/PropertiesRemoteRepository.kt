package com.zap.zaprealestate.model.remote

import com.zap.zaprealestate.model.Property
import com.zap.zaprealestate.model.PropertyRepository
import com.zap.zaprealestate.model.remote.models.PropertyResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class PropertiesRepositoryImpl : PropertyRepository {

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

    override fun getProperties(onError: (() -> Unit)?, onSuccess: (List<Property>) -> Unit) {

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
                        propertiesResponse?.map { propertyResponse ->
                            propertyResponse.run { Property(id, images) }
                        } ?: emptyList()
                    onSuccess(properties)
                }

            })
    }
}

interface PropertiesAPI {
    @GET("source-1.json")
    fun getProperties(): Call<List<PropertyResponse>?>?
}