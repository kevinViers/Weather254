package com.C2PG.Weather254

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.C2PG.Weather254.databinding.ActivityGameBinding
import com.C2PG.Weather254.databinding.ActivityMainBinding
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
        setContentView(R.layout.activity_game)

        // Inflate the layout using view binding
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val compareList = mutableListOf<Double>()

        var check: Boolean = false
        for (i in 1..2)
        {
            compareList.add(randZip(i))
        }

        binding.button.setOnClickListener {
            if (compareList[0] > compareList[1])
            {

            }
        }
    }
    private fun gameCompare(){

    }

    private fun getMyData(zipCode: String, cityNum: Int): Double {
        // Create a retrofit builder with the base URL and Gson converter
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(bURL)
            .build()
            .create(ApiInterface::class.java)

        //Value that will be checked
        var returnThis = -1.004

        // Get the weather data for the given zip code
        val retrofitData = retrofitBuilder.getData(zipCode)

        // Handle the response using a callback
        retrofitData.enqueue(object : Callback<weatherData> {
            override fun onResponse(call: Call<weatherData>, response: Response<weatherData>) {
                // If the response is successful and not for the game, show the weather data
                if (response.isSuccessful) {
                    if (cityNum == 1)
                    {
                        binding.city1.text = response.body()!!.location.name
                    }
                    else
                    {
                        binding.city2.text = response.body()!!.location.name
                    }

                    returnThis = response.body()!!.current.temp_f
                }
                // Otherwise, display an error message
                else {
                    returnThis = -1.004
                }
            }

            // Handle API call failure
            override fun onFailure(call: Call<weatherData>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Call Error, Check connection",
                    Toast.LENGTH_LONG
                ).show()
                returnThis = -1.004
            }
        })

        return returnThis
    }

    private fun randZip(cityNum: Int): Double {
        var check: Boolean = false
        val random = Random
        val zipCode = random.nextInt(99999 - 10000) + 10000
        val newZipCode = String.format("%05d", zipCode)

        while (!check)
        {
            zip = randZip(i)
            if (randZip(i) != -1.04)
            {
                compareList[i] = randZip(i)
                check = true
            }
        }


        return getMyData(newZipCode, cityNum)
    }
}
