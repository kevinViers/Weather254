package com.C2PG.Weather254

import retrofit2.Call
import retrofit2.http.GET

//Key to call, don't change
private const val apiKey = "?key=627b37bf9c7949a2a8042815231104"

interface ApiInterface {

//    Documentation on each method at https://www.weatherapi.com/docs/ but forecast just returns
//    the next three days along with the current, can be set to current for just current
//    val apiEndPoint = "current.json"
//
//    ZIP or City Name  -> could be set to q=auto:ip  to auto grab location (not sure how this works tbh)
//    var location:String = "&q=92831"
//
//    days forecast, THREE
//    val days = "&days=3"
//
//    if no, API call doesn't grab air quality data
//    var airQuality = "&aqi=yes"
//
//    Url used for api call
//    Base url is on mainactivity and shouldnt be changed, same for key
//    val apiURL = apiEndPoint + apiKey + location + days + airQuality

    @GET("forecast.json?key=627b37bf9c7949a2a8042815231104&q=London&days=3&aqi=yes")
    fun getData(): Call<weatherData>
}