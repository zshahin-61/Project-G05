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
import retrofit2.http.Query

interface ApiService {

  //apiCode : ooNeXJZPx1Q5JhfDWIxiRp5eBtYdlt27EPynnd8b
  // url parameter variable --> Retrofit Param annotion
//    @GET("api/v1/parks/?stateCode={stateCode}&api_key=ooNeXJZPx1Q5JhfDWIxiRp5eBtYdlt27EPynnd8b")
//    suspend fun getUsaNationalParksbyState(@Path("stateCode") stateCode:String)
//            : Response<UsaNationalParks>
  @GET()    //("api/v1/parks?api_key=ooNeXJZPx1Q5JhfDWIxiRp5eBtYdlt27EPynnd8b")
  suspend fun getUsaNationalParksbyState(@Query("stateCode") stateCode:String): Response<UsaNationalParks>

}