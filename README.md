# MarsRealEstate 
==============================

- Made with Google Code Labs!
- Retrofit
- Moshi

Introduction
------------

MarsRealEstate is a demo app that shows available properties for sale and for rent on Mars.
The property data is stored on a Web server as a REST web service.  This app demonstrated
the use of [Retrofit](https://square.github.io/retrofit/) to make REST requests to the 
web service, [Moshi](https://github.com/square/moshi) to handle the deserialization of the 
returned JSON to Kotlin data objects, and [Glide](https://bumptech.github.io/glide/) to load and 
cache images by URL.  

The app also leverages [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel),
[LiveData](https://developer.android.com/topic/libraries/architecture/livedata), 
[Data Binding](https://developer.android.com/topic/libraries/data-binding/) with binding 
adapters, and [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/) 
with the SafeArgs plugin for parameter passing between fragments.

Pre-requisites
--------------

You need to know:
- How to create and use fragments.
- How to navigate between fragments, and use safeArgs to pass data between fragments.
- How to use architecture components including ViewModel, ViewModelProvider.Factory, LiveData, and LiveData transformations.
- How to use coroutines for long-running tasks.

#### Overview
- The app has a ViewModel for each fragment. For this codelab, you create a layer for the network service, and the ViewModel communicates directly with that network layer. This is similar to what you did in previous codelabs when the ViewModel communicated with the Room database.
- The overview ViewModel is responsible for making the network call to get the Mars real estate information. The detail ViewModel holds details for the single piece of Mars real estate that's displayed in the detail fragment. For each ViewModel, you use LiveData with lifecycle-aware data binding to update the app UI when the data changes.
- You use the Navigation component to both navigate between the two fragments, and to pass the selected property as an argument.

