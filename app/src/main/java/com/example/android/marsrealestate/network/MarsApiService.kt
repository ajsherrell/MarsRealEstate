/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//filter the service
enum class MarsApiFilter(val value: String) {
    SHOW_RENT("rent"),
    SHOW_BUY("buy"),
    SHOW_ALL("all")
}

private const val BASE_URL = " https://android-kotlin-fun-mars-server.appspot.com/"

private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BASE_URL)
        .build()

//define an interface that defines how Retrofit talks to the web server using HTTP requests.
interface MarsApiService {
    @GET("realestate")
    fun getProperties(@Query("filter") type: String):
            Deferred<List<MarsProperty>>
}
/** Right now the goal is to get the JSON response string from the web service,
 * and you only need one method to do that: getProperties(). To tell Retrofit what this method should do,
 * use a @GET annotation and specify the path, or endpoint, for that web service method.
 * In this case the endpoint is called realestate. When the getProperties() method is invoked,
 * Retrofit appends the endpoint realestate to the base URL (which you defined in the Retrofit builder),
 * and creates a Call object. That Call object is used to start the request.
 *
 * The @Query annotation tells the getProperties() method (and thus Retrofit)
 * to make the web service request with the filter option. Each time getProperties()
 * is called, the request URL includes the ?filter=type portion, which directs the web
 * service to respond with results that match that query.*/

//define a public object called MarsApi to initialize the Retrofit service.
object MarsApi {
    val retrofitService: MarsApiService by lazy {
        retrofit.create(MarsApiService::class.java)
    }
}
/** The Retrofit create() method creates the Retrofit service itself with the MarsApiService interface.
 *  Because this call is expensive, and the app only needs one Retrofit service instance,
 *  you expose the service to the rest of the app using a public object called MarsApi,
 *  and lazily initialize the Retrofit service there. Now that all the setup is done,
 *  each time your app calls MarsApi.retrofitService,
 *  it will get a singleton Retrofit object that implements MarsApiService.*/
