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

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the most recent response
    private val _response = MutableLiveData<String>()

    // The external immutable LiveData for the response String
    val response: LiveData<String>
        get() = _response

    //coroutine job
    private var viewModelJob = Job()

    //coroutine scope
    private val coroutineScope = CoroutineScope(
            viewModelJob + Dispatchers.Main
    )

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties()
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     * Because the response: is a LiveData and we've set the lifecycle for the binding variable,
     * any changes to it will update the app UI. The MarsApi.retrofitService.getProperties() method
     * returns a Call object. Then you can call enqueue() on that object to start the network request on a
     * background thread.
     */
    private fun getMarsRealEstateProperties() { //Calling getProperties() from the MarsApi service creates and starts the network call on a background thread, returning the Deferred object for that task.
        coroutineScope.launch {
            var getPropertiesDeferred = MarsApi.retrofitService.getProperties()

            try {
                var listResult = getPropertiesDeferred.await()
                _response.value = "Success: ${listResult.size} Mars properties retrieved!"
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

/** private fun getMarsRealEstateProperties() {
MarsApi.retrofitService.getProperties().enqueue(
object: Callback<List<MarsProperty>> {
override fun onFailure(call: Call<List<MarsProperty>>, t: Throwable) {
_response.value = "Failure: ${t.message}" //The onFailure() callback is called when the web service response fails. For this response, set the _response status to "Failure: " concatenated with the message from the Throwable argument.
}

override fun onResponse(call: Call<List<MarsProperty>>, response: Response<List<MarsProperty>>) {
_response.value = "Success: ${response.body()?.size} Mars properties retrieved!"
}

}
)
} --using coroutines instead.
        */