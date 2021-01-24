package com.palchak.sergey.kotlincoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.palchak.sergey.kotlincoroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.button.setOnClickListener {
            setNewText("Clicked!")

            fakeApiRequest()
        }
    }

    private fun fakeApiRequest() {
        CoroutineScope(IO).launch {
            val job1 = launch {
                val time1 = measureTimeMillis {
                    println("debug: Launching job1 in thread ${Thread.currentThread().name}")
                    val result1 = getResult1FromApi()
                    setTextOnMainThread(result1)
                }
                println("debug: Completed job1 in $time1 ms")
            }
            val job2 = launch {
                val time2 = measureTimeMillis {
                    println("debug: Launching job2 in thread ${Thread.currentThread().name}")
                    val result2 = getResult2FromApi()
                    setTextOnMainThread(result2)
                }
                println("debug: Completed job1 in $time2 ms")
            }
        }
    }

    private fun setNewText(input: String) {
        val newText = binding.text.text.toString() + "$input\n"
        binding.text.text = newText
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