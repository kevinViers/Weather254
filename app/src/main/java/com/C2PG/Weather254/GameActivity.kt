package com.C2PG.Weather254

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.C2PG.Weather254.R.*
import com.C2PG.Weather254.databinding.ActivityGameBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.random.Random
import kotlinx.coroutines.*

private const val bURL = "https://api.weatherapi.com/v1/"

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var streak = 0

        binding.homeButton.setOnClickListener {
            startActivity(Intent(this@GameActivity, MainActivity::class.java))
        }

        updateCities()

        binding.colderButton.setOnClickListener {
            val city1Temp = binding.city1.tag as? Double ?: Double.NEGATIVE_INFINITY
            val city2Temp = binding.city2.tag as? Double ?: Double.POSITIVE_INFINITY

            if (city1Temp <= city2Temp) {
                streak += 1
                binding.streakPoints.text = String.format("%d", streak)
                binding.colderButton.setBackgroundColor(color.green)
            } else {
                streak = 0
                binding.streakPoints.text = String.format("%d", streak)
            }

            updateCities()
        }

        binding.hotterButton.setOnClickListener {
            val city1Temp = binding.city1.tag as? Double ?: Double.NEGATIVE_INFINITY
            val city2Temp = binding.city2.tag as? Double ?: Double.POSITIVE_INFINITY

            if (city1Temp >= city2Temp) {
                streak += 1
                binding.streakPoints.text = String.format("%d", streak)
                binding.hotterButton.setBackgroundColor(color.green)
            } else {
                streak = 0
                binding.streakPoints.text = String.format("%d", streak)
            }

            updateCities()
        }
    }

    private fun updateCities() {
        binding.colderButton.setBackgroundColor(color.blue)
        binding.hotterButton.setBackgroundColor(color.red)

        getMyData(1) { city1Data ->
            binding.city1.text = city1Data.location.name +  ", " + city1Data.location.region
            binding.city1.tag = city1Data.current.temp_f
            getMyData(2) { city2Data ->
                binding.city2.text = city2Data.location.name + ", " + city2Data.location.region
                binding.city2.tag = city2Data.current.temp_f
            }
        }
    }

    private fun getMyData(cityNum: Int, onSuccess: (currentGameData) -> Unit) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(bURL)
            .build()
            .create(ApiInterface::class.java)

        val random = Random
        val zipCode = random.nextInt(99999 - 10000) + 10000
        val newZipCode = String.format("%05d", zipCode)

        val retrofitData = retrofitBuilder.getCurrentData(newZipCode)

        retrofitData.enqueue(object : Callback<currentGameData> {
            override fun onResponse(call: Call<currentGameData>, response: Response<currentGameData>) {
                if (response.isSuccessful) {
                    onSuccess(response.body()!!)
                } else {
                    getMyData(cityNum, onSuccess)
                }
            }
            override fun onFailure(call: Call<currentGameData>, t: Throwable) {
                Toast.makeText(applicationContext, "Call Error, Check connection", Toast.LENGTH_LONG).show()
            }
        })
    }

}
