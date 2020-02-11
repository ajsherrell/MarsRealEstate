# MarsRealEstate 
==============================

- Made with Google Code Labs!
- Retrofit
- REST
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

## REST
- Representational State Transfer
- The Mars real estate data is stored on a web server, as a REST web service. Web services use the REST architecture are built using standard web components and protocols.
- You make a request to a web service in a standardized way via URIs. The familiar web URL is actually a type of URI, and both are used interchangeably throughout this course. For example, in the app for this lesson, you retrieve all the data from the following server:

https://android-kotlin-fun-mars-server.appspot.com

If you type the following URL in your browser, you get a list of all available real estate properties on Mars! https://android-kotlin-fun-mars-server.appspot.com/realestate

- A collection of JSON objects is a JSON array, and it's the array you get back as a response from a web service.

To get this data into the app, your app needs to establish a network connection and communicate with that server, and then receive and parse the response data into a format the app can use. In this codelab, you use a REST client library called Retrofit to make this connection

### Retrofit
- Retrofit creates a network API for the app based on the content from the web service. It fetches data from the web service and routes it through a separate converter library that knows how to decode the data and return it in the form of useful objects. Retrofit includes built-in support for popular web data formats such as XML and JSON. Retrofit ultimately creates most of the network layer for you, including critical details such as running the requests on background threads.

The MarsApiService class holds the network layer for the app; that is, this is the API that your ViewModel will use to communicate with the web service. This is the class where you will implement the Retrofit service API.

- Retrofit needs at least two things available to it to build a web services API: the base URI for the web service, and a converter factory. The converter tells Retrofit what do with the data it gets back from the web service. In this case, you want Retrofit to fetch a JSON response from the web service, and return it as a String. Retrofit has a ScalarsConverter that supports strings and other primitive types, so you call addConverterFactory() on the builder with an instance of ScalarsConverterFactory. Finally, you call build() to create the Retrofit object. Scalar helps if you want to display raw Json data. 

- Coroutines: Call adapters add the ability for Retrofit to create APIs that return something other than the default Call class. In this case, the CoroutineCallAdapterFactory allows us to replace the Call object that getProperties() returns with a Deferred object instead.
- The Deferred interface defines a coroutine job that returns a result value (Deferred inherits from Job). The Deferred interface includes a method called await(), which causes your code to wait without blocking until the value is ready, and then that value is returned. The Dispatchers.Main dispatcher uses the UI thread for its work. Because Retrofit does all its work on a background thread, there's no reason to use any other thread for the scope. This allows you to easily update the value of the MutableLiveData when you get a result.
- Calling await() on the Deferred object returns the result from the network call when the value is ready. The await() method is non-blocking, so the Mars API service retrieves the data from the network without blocking the current thread—which is important because we're in the scope of the UI thread. Once the task is done, your code continues executing from where it left off. This is inside the try {} so that you can catch exceptions.

### Moshi
- an Android JSON parser that converts a JSON string into Kotlin objects. Retrofit has a converter that works with Moshi, so it's a great library for your purposes here.

#### Summary
- REST web services:
A web service is a service on the internet that enables your app to make requests and get data back.
Common web services use a REST architecture. Web services that offer REST architecture are known as RESTful services. RESTful web services are built using standard web components and protocols.
You make a request to a REST web service in a standardized way, via URIs.
To use a web service, an app must establish a network connection and communicate with the service. Then the app must receive and parse response data into a format the app can use.
The Retrofit library is a client library that enables your app to make requests to a REST web service.
Use converters to tell Retrofit what do with data it sends to the web service and gets back from the web service. For example, the ScalarsConverter converter treats the web service data as a String or other primitive.
To enable your app to make connections to the internet, add the "android.permission.INTERNET" permission in the Android manifest.

- JSON parsing:
The response from a web service is often formatted in JSON, a common interchange format for representing structured data.
A JSON object is a collection of key-value pairs. This collection is sometimes called a dictionary, a hash map, or an associative array.
A collection of JSON objects is a JSON array. You get a JSON array as a response from a web service.
The keys in a key-value pair are surrounded by quotes. The values can be numbers or strings. Strings are also surrounded by quotes.
The Moshi library is Android JSON parser that converts a JSON string into Kotlin objects. Retrofit has a converter that works with Moshi.
Moshi matches the keys in a JSON response with properties in a data object that have the same name.
To use a different property name for a key, annotate that property with the @Json annotation and the JSON key name.

- Retrofit and coroutines:
Call adapters let Retrofit create APIs that return something other than the default Call class. Use the CoroutineCallAdapterFactory class to replace the Call with a coroutine Deferred.
Use the await() method on the Deferred object to cause your coroutine code to wait without blocking until the value is ready, and then the value is returned.

