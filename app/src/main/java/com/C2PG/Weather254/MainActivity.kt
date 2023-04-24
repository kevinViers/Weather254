package com.C2PG.Weather254

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.C2PG.Weather254.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

//baseUrl
private const val bURL = "https://api.weatherapi.com/v1/"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Set a click listener on the search button
        binding.searchButton.setOnClickListener {
            // Get the zip code entered by the user
            val zipCode = binding.zipCodeInput.text.toString()

            // Call getMyData() with the entered zip code
            getMyData(zipCode, false)
        }

        // Call getMyData() with a default zip code
        getMyData("92831", false)
    }

    private fun getMyData(zipCode: String, game: Boolean) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(bURL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getData(zipCode)

        retrofitData.enqueue(object: Callback<weatherData>
        {
            override fun onResponse(call: Call<weatherData>, response: Response<weatherData>)
            {
                if (response.isSuccessful && !game) {
                    //Variable that contains all data
                    val responseBody = response.body()!!
                    showData(responseBody=responseBody)
                }
                else if (response.isSuccessful && game)
                {
                    val temp = response.body()!!.current.temp_f
                    binding.currentTempTextView.text = temp.toString() + "°F"
                }
                else if (!response.isSuccessful && game)
                {
                    randZip()
                }
                else
                {
                    Toast.makeText(applicationContext, "Zipcode Error", Toast.LENGTH_LONG).show()
                }

            }
            override fun onFailure(call: Call<weatherData>, t:Throwable)
            {
                Toast.makeText(applicationContext, "Call Error, Check connection", Toast.LENGTH_LONG).show()
            }
        })
    }
    private fun showData(responseBody: weatherData) {
        binding.headerLocationName.text = responseBody.location.name
        binding.currentTempTextView.text = "${responseBody.current.temp_f}°F"
    }


    //call twice when game is clicked to generate two zips
    private fun randZip()
    {
        val random = Random
        val zipCode = random.nextInt(99999 - 10000) + 10000
        val newZipCode = String.format("%05d", zipCode)

        getMyData(newZipCode, true)
    }
}
