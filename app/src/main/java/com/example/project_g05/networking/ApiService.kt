package com.example.project_g05.networking

import com.example.project_g05.models.NationalPark
import com.example.project_g05.models.UsaNationalParks
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    //////////////Golnaz apiCode : ooNeXJZPx1Q5JhfDWIxiRp5eBtYdlt27EPynnd8b

    // url parameter variable --> Retrofit Param annotion
    @GET("api/v1/parks/?stateCode={stateCode}&api_key={apiCode}")
    suspend fun getUsaNationalParksbyState(@Path("stateCode") categoryName:String, @Path("apiCode") apiCode:String)
            : Response<UsaNationalParks>

}
