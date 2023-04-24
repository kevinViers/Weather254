// Define the package name
package com.C2PG.Weather254

// Import necessary classes and libraries
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.C2PG.Weather254.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random

// Define the base URL for the API
private const val bURL = "https://api.weatherapi.com/v1/"

// Define the main activity class
class MainActivity : AppCompatActivity() {

    // Declare variables for view binding and text display
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentTempTextView: TextView

    // Define the activity creation function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the text view for displaying current temperature
        currentTempTextView = binding.currentTempTextView

        // Set a click listener on the search button
        binding.searchButton.setOnClickListener {
            // Get the zip code entered by the user
            val zipCode = binding.zipCodeInput.text.toString()

            // Call the getMyData function with the entered zip code
            getMyData(zipCode, false)
        }

        // Call the getMyData function with a default zip code
        getMyData("92831", false)
    }

    // Define a function to retrieve weather data from the API
    private fun getMyData(zipCode: String, game: Boolean) {
        // Create a retrofit builder with the base URL and Gson converter
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(bURL)
            .build()
            .create(ApiInterface::class.java)

        // Get the weather data for the given zip code
        val retrofitData = retrofitBuilder.getData(zipCode)

        // Handle the response using a callback
        retrofitData.enqueue(object: Callback<weatherData> {
            override fun onResponse(call: Call<weatherData>, response: Response<weatherData>) {
                // If the response is successful and not for the game, show the weather data
                if (response.isSuccessful && !game) {
                    val responseBody = response.body()!!
                    showData(responseBody)
                }
                // If the response is successful and for the game, update the current temperature text view
                else if (response.isSuccessful && game) {
                    val temp = response.body()!!.current.temp_f
                    currentTempTextView.text = temp.toString() + "°F"
                }
                // If the response is not successful and for the game, generate a random zip code and try again
                else if (!response.isSuccessful && game) {
                    randZip()
                }
                // Otherwise, display an error message
                else {
                    Toast.makeText(applicationContext, "Zipcode Error", Toast.LENGTH_LONG).show()
                }
            }

            // Handle API call failure
            override fun onFailure(call: Call<weatherData>, t:Throwable) {
                Toast.makeText(applicationContext, "Call Error, Check connection", Toast.LENGTH_LONG).show()
            }
        })
    }

    // Define a function to display weather data
    private fun showData(responseBody: weatherData) {
        binding.headerLocationName.text = responseBody.location.name
        currentTempTextView.text = "${responseBody.current.temp_f}°F"
    }

    // Call twice when game is clicked to generate two zips
    private fun randZip() {
        val random = Random
        val zipCode = random.nextInt(99999 - 10000) + 10000
        val newZipCode = String.format("%05d", zipCode)

        getMyData(newZipCode, true)
    }
}
