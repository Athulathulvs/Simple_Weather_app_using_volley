package com.example.simple_weather_app

import android.accounts.NetworkErrorException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.NetworkError
import com.android.volley.NoConnectionError
import com.android.volley.ParseError
import com.android.volley.Request
import com.android.volley.ServerError
import com.android.volley.TimeoutError
import com.android.volley.toolbox.Authenticator
import com.android.volley.toolbox.JsonObjectRequest
import com.example.simple_weather_app.databinding.ActivityMainBinding
const val TAG = "Weather"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        binding.btnFindWeather.setOnClickListener {
            val city = binding.edtCity.text.toString()
            if(city.isBlank()){
                return@setOnClickListener
            }
            sendRequest(city)
        }
    }

    private fun sendRequest(city: String) {
        val url =
           " https://api.weatherapi.com/v1/current.json?key=3d332b1bd7cb4ef9a0651651231410&q=$city&aqi=no"
        val requestObj =
            JsonObjectRequest(
                Request.Method.GET,url,null,
                { response ->
                    val locationObj = response.getJSONObject("location")
                    val region = locationObj.getString("region")
                    val country = locationObj.getString("country")

                    val currentObj =response.getJSONObject("current")
                    val temp_C =currentObj.getDouble("temp_c")
                    val temp_F =currentObj.getDouble("temp_f")

                  binding.txtWeatherInfo.text ="Region : $region\nCountry : $country\ntemp_c : $temp_C\ntemp_f : $temp_F"
                },
                { error ->
                    val message = when (error) {
                        is NetworkError  -> "Network error occurred"
                        is ServerError -> "Server error occurred "
                        is ParseError -> "Parse error"
                        is AuthFailureError -> "Authentification error"
                        is NoConnectionError -> "No connection"
                        is TimeoutError -> "Requested time out"
                        else -> "An error occurred"
                    }
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                })
        MySingleton.getInstance(this).addToRequestQueue(requestObj)
    }
}