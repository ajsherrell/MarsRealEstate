# MarsRealEstate 
==============================

- Made with Google Code Labs!
- Retrofit
- REST
- Moshi
- Glide
- Binding Adapter
- Parcelable

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

### Glide
- you can use a community-developed library called Glide to download, buffer, decode, and cache your images. Glide leaves you with a lot less work than if you had to do all of this from scratch.

Glide basically needs two things:

The URL of the image you want to load and show.
An ImageView object to display that image.
- Binding Adapter: you have the URL of an image to display, and it's time to start working with Glide to load that image. In this step, you use a binding adapter to take the URL from an XML attribute associated with an ImageView, and you use Glide to load the image. Binding adapters are extension methods that sit between a view and bound data to provide custom behavior when the data changes. In this case, the custom behavior is to call Glide to load an image from a URL into an ImageView.
- You want the final Uri object to use the HTTPS scheme, because the server you pull the images from requires that scheme. To use the HTTPS scheme, append buildUpon.scheme("https") to the toUri builder. The toUri() method is a Kotlin extension function from the Android KTX core library, so it just looks like it's part of the String class.

### Parcelable
- The Parcelable interface enables objects to be serialized, so that the objects' data can be passed around between fragments or activities. In this case, for the data inside the MarsProperty object to be passed to the detail fragment via Safe Args, MarsProperty must implement the Parcelable interface. The good news is that Kotlin provides an easy shortcut for implementing that interface. Add the @Parcelize annotation to the class definition. The @Parcelize annotation uses the Kotlin Android extensions to automatically implement the methods in the Parcelable interface for this class. You don't have to do anything else!

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

- To simplify the process of managing images, use the Glide library to download, buffer, decode, and cache images in your app.
Glide needs two things to load an image from the internet: the URL of an image, and an ImageView object to put the image in. To specify these options, use the load() and into() methods with Glide.
Binding adapters are extension methods that sit between a view and that view's bound data. Binding adapters provide custom behavior when the data changes, for example, to call Glide to load an image from a URL into an ImageView.
Binding adapters are extension methods annotated with the @BindingAdapter annotation.
To add options to the Glide request, use the apply() method. For example, use apply() with placeholder() to specify a loading drawable, and use apply() with error() to specify an error drawable.
To produce a grid of images, use a RecyclerView with a GridLayoutManager.
To update the list of properties when it changes, use a binding adapter between the RecyclerView and the layout.
- Binding expressions:
Use binding expressions in XML layout files to perform simple programmatic operations, such as math or conditional tests, on bound data.
To reference classes inside your layout file, use the <import> tag inside the <data> tag.
- Web service query options:
Requests to web services can include optional parameters.
To specify query parameters in the request, use the @Query annotation in Retrofit.

