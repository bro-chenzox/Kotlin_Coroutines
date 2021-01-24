package com.palchak.sergey.kotlincoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.palchak.sergey.kotlincoroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    private fun setNewText(input: String) {
        binding.text.text = input
    }

    private suspend fun setTextOnMainThread(input: String) {
        withContext(Main) {
            setNewText(input)
        }
    }

    private suspend fun getResult1FromApi(): String {
        delay(1000)
        return "Result1"
    }

    private suspend fun getResult2FromApi(): String {
        delay(1700)
        return "Result2"
    }
}