package com.palchak.sergey.kotlincoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.palchak.sergey.kotlincoroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
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
            val executionTime = measureTimeMillis {
                val result1: Deferred<String> = async {
                    println("debug: launching job1 on tread ${Thread.currentThread().name}")
                    getResult1FromApi()
                }

                val result2: Deferred<String> = async {
                    println("debug: launching job2 on tread ${Thread.currentThread().name}")
                    getResult2FromApi()
                }

                setTextOnMainThread(result1.await())
                setTextOnMainThread(result2.await())
            }
            println("debug: Total elapsed time is $executionTime ms")
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