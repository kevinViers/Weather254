package com.C2PG.Weather254

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.C2PG.Weather254.databinding.ActivityGameBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

private const val bURL = "https://api.weatherapi.com/v1/"

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding
        System.gc()
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)



        var streak = 0

        binding.homeButton.setOnClickListener {
            startActivity(Intent(this@GameActivity, MainActivity::class.java))
        }
        var check: Boolean = false
        var city1 = getMyData(1)
        var city2 = getMyData(2)

        //Initial setup (city names and temp)
        while (!check)
        {
            when(city1)
            {
                null -> city1 = getMyData(1)
                else -> check = true
            }
        }
        check = false
        while (!check)
        {
            when(city2)
            {
                null -> city2 = getMyData(1)
                else -> check = true
            }
        }

        binding.colderButton.setOnClickListener {
            if ((city1?.current?.temp_f ?:Double.MIN_VALUE) <= (city2?.current?.temp_f ?: Double.MIN_VALUE))
            {
                streak +=1
                binding.streakPoints.text = String.format("%d", streak)
                binding.colderButton.setBackgroundResource(R.color.green)
            }
            else
            {
                streak = 0
                binding.streakPoints.text = String.format("%d", streak)
            }

            while (!check)
            {
                when(city1)
                {
                    null -> city1 = getMyData(1)
                    else -> check = true
                }
            }
            check = false
            while (!check)
            {
                when(city2)
                {
                    null -> city2 = getMyData(1)
                    else -> check = true
                }
            }
        }
        binding.hotterButton.setOnClickListener {
            if ((city1?.current?.temp_f ?:Double.MIN_VALUE) >= (city2?.current?.temp_f ?: Double.MIN_VALUE))
            {
                streak +=1
                binding.streakPoints.text = String.format("%d", streak)
                binding.hotterButton.setBackgroundResource(R.color.green)
            }
            else
            {
                streak = 0
                binding.streakPoints.text = String.format("%d", streak)
            }

            while (!check)
            {
                when(city1)
                {
                    null -> city1 = getMyData(1)
                    else -> check = true
                }
            }
            check = false
            while (!check)
            {
                when(city2)
                {
                    null -> city2 = getMyData(1)
                    else -> check = true
                } } }

    }
    private fun getMyData(cityNum: Int): weatherData? {
        // Create a retrofit builder with the base URL and Gson converter
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(bURL)
            .build()
            .create(ApiInterface::class.java)

        val random = Random
        val zipCode = random.nextInt(99999 - 10000) + 10000
        val newZipCode = String.format("%05d", zipCode)

        var responseBody: weatherData? = null

        // Get the weather data for the given zip code
        val retrofitData = retrofitBuilder.getData(newZipCode)

        // Handle the response using a callback
        retrofitData.enqueue(object : Callback<weatherData> {
            override fun onResponse(call: Call<weatherData>, response: Response<weatherData>) {
                // If the response is successful and not for the game, show the weather data
                if (response.isSuccessful) {
                    if (cityNum == 1) {
                        binding.colderButton.setBackgroundResource(R.color.black)
                        binding.hotterButton.setBackgroundResource(R.color.black)
                        binding.city1.text = response.body()!!.location.name
                    }
                    else {
                        binding.city2.text = response.body()!!.location.name
                    }
                    responseBody = response.body()!!
                }}
            // Handle API call failure
            override fun onFailure(call: Call<weatherData>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Call Error, Check connection",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
        return responseBody
    }
}
