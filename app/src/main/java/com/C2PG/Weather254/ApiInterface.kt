package com.C2PG.Weather254

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val keyForecast = "627b37bf9c7949a2a8042815231104"
private const val keyCurrent = "302baf19e4d14533b2130228232704"
interface ApiInterface {
    //Query takes a parameter which should match links query, and takes in a variable
    //Query output -> "&parameter=variable" into URL, query placement in URL doesn't matter
@GET("forecast.json?key=627b37bf9c7949a2a8042815231104&days=3&aqi=yes")
fun getForecastData(@Query("q") location:String): Call<weatherData>

//Used to return only super basic information for game (current temp of location)
@GET("current.json?key=302baf19e4d14533b2130228232704")
    fun getCurrentData(@Query("q") location:String) : Call<currentGameData>
}

