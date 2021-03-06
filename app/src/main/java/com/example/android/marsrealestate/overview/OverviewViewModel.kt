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
import com.example.android.marsrealestate.network.MarsApiFilter
import com.example.android.marsrealestate.network.MarsProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

enum class MarsApiStatus {
    LOADING,
    ERROR,
    DONE
}
//The loading state happens while you're waiting for data in the call to await().
/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the most recent response
    private val _status = MutableLiveData<MarsApiStatus>()

    // The external immutable LiveData for the response String
    val status: LiveData<MarsApiStatus>
        get() = _status

    //include live data for a single mars property
    private val _properties = MutableLiveData<List<MarsProperty>>()

    val properties: LiveData<List<MarsProperty>>
        get() = _properties

    //coroutine job
    private var viewModelJob = Job()

    //coroutine scope
    private val coroutineScope = CoroutineScope(
            viewModelJob + Dispatchers.Main
    )

    //for navigation
    private val _navigateToSelectedProperty = MutableLiveData<MarsProperty>()
    val navigateToSelectedProperty: LiveData<MarsProperty>
        get() = _navigateToSelectedProperty

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     * Because the response: is a LiveData and we've set the lifecycle for the binding variable,
     * any changes to it will update the app UI. The MarsApi.retrofitService.getProperties() method
     * returns a Call object. Then you can call enqueue() on that object to start the network request on a
     * background thread.
     */
    private fun getMarsRealEstateProperties(filter: MarsApiFilter) { //Calling getProperties() from the MarsApi service creates and starts the network call on a background thread, returning the Deferred object for that task.
        coroutineScope.launch {
            var getPropertiesDeferred = MarsApi.retrofitService.getProperties(filter.value)

            try {
                _status.value = MarsApiStatus.LOADING
                var listResult = getPropertiesDeferred.await()
                _status.value = MarsApiStatus.DONE
                if (listResult.isNotEmpty()) {
                    _properties.value = listResult
                }
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun updateFilter(filter: MarsApiFilter) {
        getMarsRealEstateProperties(filter)
    }

    //method to take the selected property when navigating
    fun displayPropertyDetails(marsProperty: MarsProperty) {
        _navigateToSelectedProperty.value = marsProperty
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    } //mark the navigation state to complete, and to avoid the navigation being triggered again when the user returns from the detail view.
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