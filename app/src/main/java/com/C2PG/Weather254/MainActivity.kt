package com.C2PG.Weather254

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//baseUrl
private const val bURL = "https://api.weatherapi.com/v1/"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getMyData()
    }

    private fun getMyData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(bURL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData("fullerton", "yes")

        retrofitData.enqueue(object: Callback<weatherData>
        {
            override fun onResponse(call: Call<weatherData>, response: Response<weatherData>) {
                //Variable that contains all data
                val responseBody = response.body()!!

                //test variable to show how to access info
                val test = responseBody.current.cloud
        }
            override fun onFailure(call: Call<weatherData>, t:Throwable)
            {
                print("API call failed")
            }
        })


    }

}