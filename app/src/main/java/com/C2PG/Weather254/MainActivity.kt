// Define the package name
package com.C2PG.Weather254

// Import necessary classes and libraries
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.C2PG.Weather254.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Define the base URL for the API
private const val bURL = "https://api.weatherapi.com/v1/"
private const val API: String = "e55db302eb8c403a89801251232504"

// Define the main activity class
class MainActivity : AppCompatActivity() {
    // Declare variables for view binding and text display
    private lateinit var binding: ActivityMainBinding
    private lateinit var currentTempTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var stateTextView: TextView
    private lateinit var maxTemp: TextView
    private lateinit var minTemp: TextView
    // Declare variables for forecast in main activity
    private lateinit var timeForecast1: TextView
    private lateinit var timeForecast2: TextView
    private lateinit var timeForecast3: TextView
    private lateinit var timeForecast4: TextView
    private lateinit var tempForecast1: TextView
    private lateinit var tempForecast2: TextView
    private lateinit var tempForecast3: TextView
    private lateinit var tempForecast4: TextView
    private lateinit var humidity1: TextView
    private lateinit var humidity2: TextView
    private lateinit var humidity3: TextView
    private lateinit var humidity4: TextView
    private lateinit var windForecast1: TextView
    private lateinit var windForecast2: TextView
    private lateinit var windForecast3: TextView
    private lateinit var windForecast4: TextView
    private lateinit var airPressure1: TextView
    private lateinit var airPressure2: TextView
    private lateinit var airPressure3: TextView
    private lateinit var airPressure4: TextView


    // Define the activity creation function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout using view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the text view for displaying current temperature
        currentTempTextView = binding.currentTempTextView

        // Get the text view for displaying min and max temperature
        maxTemp = binding.maxTempTextView
        minTemp = binding.minTempTextView

        // Get the text view for displaying forecast info
        timeForecast1 = binding.timeForecast1
        timeForecast2 = binding.timeForecast2
        timeForecast3 = binding.timeForecast3
        timeForecast4 = binding.timeForecast4
        tempForecast1 = binding.tempForecast1
        tempForecast2 = binding.tempForecast2
        tempForecast3 = binding.tempForecast3
        tempForecast4 = binding.tempForecast4
        humidity1 = binding.humidity1
        humidity2 = binding.humidity2
        humidity3 = binding.humidity3
        humidity4 = binding.humidity4
        windForecast1 = binding.windForcast1
        windForecast2 = binding.windForcast2
        windForecast3 = binding.windForcast3
        windForecast4 = binding.windForcast4
        airPressure1 = binding.airPressure1
        airPressure2 = binding.airPressure2
        airPressure3 = binding.airPressure3
        airPressure4 = binding.airPressure4

        //If Game button click, activity becomes GameActivity
        binding.gameButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, GameActivity::class.java))
        }

        // Set a click listener on the search button
        binding.searchButton.setOnClickListener {
            // Get the zip code entered by the user
            val zipCode = binding.zipCodeInput.text.toString()

            // Call the getMyData function with the entered zip code
            getMyData(zipCode)
        }

        // Call the getMyData function with a default zip code
        getMyData("92831")

        // Get the city and state text views
        cityTextView = binding.headerLocationNameCity
        stateTextView = binding.headerLocationNameState

        // Set the headerLocationName TextView to horizontally scroll if it's one word and too long
        binding.headerLocationName.setHorizontallyScrolling(true)
    }

    // Define a function to retrieve weather data from the API
    private fun getMyData(zipCode: String) {
        // Create a retrofit builder with the base URL and Gson converter
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(bURL)
            .build()
            .create(ApiInterface::class.java)

        // Get the weather data for the given zip code
        val retrofitData = retrofitBuilder.getData(zipCode)

        // Handle the response using a callback
        retrofitData.enqueue(object : Callback<weatherData> {
            override fun onResponse(call: Call<weatherData>, response: Response<weatherData>) {
                // If the response is successful show the weather data
                if (response.isSuccessful) {
                    val responseBody = response.body()!!
                    showData(responseBody)
                }
                // Otherwise, display an error message
                else {
                    Toast.makeText(applicationContext, "Zipcode Error", Toast.LENGTH_LONG).show()
                }
            }
            // Handle API call failure
            override fun onFailure(call: Call<weatherData>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Call Error, Check connection",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    // Define a function to display weather data
    @SuppressLint("StringFormatInvalid")
    private fun showData(responseBody: weatherData) {
        binding.headerLocationName.text = responseBody.location.name

        // Set the font size based on the length of the city name
        if (responseBody.location.name.split(" ").size == 1) {
            binding.headerLocationName.textSize = 72F
        } else {
            binding.headerLocationName.textSize = 48F
        }

        val currentTempString = getString(R.string.current_temp, responseBody.current.temp_f.toString())
        currentTempTextView.text = currentTempString

        val maxTempString = getString(R.string.temperature, responseBody.forecast.forecastday[0].day.maxtemp_f)
        maxTemp.text = maxTempString
        minTemp.text = "${responseBody.forecast.forecastday[0].day.mintemp_f}"

        // forecast within 4 hours
        timeForecast1.text = "${responseBody.forecast.forecastday[1].hour[1].time}"
        timeForecast2.text = "${responseBody.forecast.forecastday[1].hour[2].time}"
        timeForecast3.text = "${responseBody.forecast.forecastday[1].hour[3].time}"
        timeForecast4.text = "${responseBody.forecast.forecastday[1].hour[4].time}"
        tempForecast1.text = "${responseBody.forecast.forecastday[1].hour[1].temp_f}"
        tempForecast2.text = "${responseBody.forecast.forecastday[1].hour[2].temp_f}"
        tempForecast3.text = "${responseBody.forecast.forecastday[1].hour[3].temp_f}"
        tempForecast4.text = "${responseBody.forecast.forecastday[1].hour[4].temp_f}"
        humidity1.text = "${responseBody.forecast.forecastday[1].hour[1].humidity}%"
        humidity2.text = "${responseBody.forecast.forecastday[1].hour[2].humidity}%"
        humidity3.text = "${responseBody.forecast.forecastday[1].hour[3].humidity}%"
        humidity4.text = "${responseBody.forecast.forecastday[1].hour[4].humidity}%"
        windForecast1.text = "${responseBody.forecast.forecastday[1].hour[1].wind_mph} MPH"
        windForecast2.text = "${responseBody.forecast.forecastday[1].hour[2].wind_mph} MPH"
        windForecast3.text = "${responseBody.forecast.forecastday[1].hour[3].wind_mph} MPH"
        windForecast4.text = "${responseBody.forecast.forecastday[1].hour[4].wind_mph} MPH"
        airPressure1.text = "${responseBody.forecast.forecastday[1].hour[1].pressure_in}"
        airPressure2.text = "${responseBody.forecast.forecastday[1].hour[2].pressure_in}"
        airPressure3.text = "${responseBody.forecast.forecastday[1].hour[3].pressure_in}"
        airPressure4.text = "${responseBody.forecast.forecastday[1].hour[4].pressure_in}"
    }
}
