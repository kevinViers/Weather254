package com.C2PG.Weather254

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.C2PG.Weather254.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//baseUrl
private const val bURL = "https://api.weatherapi.com/v1/"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        getMyData()
    }

    private fun getMyData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(bURL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData("92602", "yes")

        retrofitData.enqueue(object: Callback<weatherData>
        {
            override fun onResponse(call: Call<weatherData>, response: Response<weatherData>) {
                //Variable that contains all data
                val responseBody = response.body()!!
                showData(responseBody=responseBody)
        }
            override fun onFailure(call: Call<weatherData>, t:Throwable)
            {
                print("API call failed")
            }
        })

    }
    private fun showData(responseBody: weatherData)
    {
        binding.headerLocationName.text = responseBody.location.name

    }
}