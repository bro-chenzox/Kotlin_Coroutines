package com.palchak.sergey.kotlincoroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
                val result1 =
                    withContext(IO) {
                        println("debug: Launching job #1 on thread ${Thread.currentThread().name}")
                        getResult1FromApi()
                    }

                val result2 =
                    withContext(IO) {
                        println("debug: Launching job #2 on thread ${Thread.currentThread().name}")
                        getResult2FromApi("fffhfuyig")
                    }
                println("debug: got result2 $result2")
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

    private suspend fun getResult2FromApi(result: String): String {
        delay(1700)
        if (result == "Result1") {
            return "Result2"
        }
        return "Result1 was incorrect..."
    }
}