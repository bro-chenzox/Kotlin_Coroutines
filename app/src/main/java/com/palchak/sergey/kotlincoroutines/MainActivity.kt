package com.palchak.sergey.kotlincoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "AppDebug"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main()
    }

    private fun main() {
        val parentJob = CoroutineScope(IO).launch {

            // ----------JOB A-----------
            val jobA = launch {
                val resultA = getDouble(1)
                println("resultA: $resultA")
            }
            jobA.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultA: $throwable")
                }
            }

            // ----------JOB B-----------
            val jobB = launch {
                val resultB= getDouble(2)
                println("resultA: $resultB")
            }
            jobB.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultB: $throwable")
                }
            }

            // ----------JOB C-----------
            val jobC = launch {
                val resultC = getDouble(3)
                println("resultC: $resultC")
            }
            jobC.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    println("Error getting resultC: $throwable")
                }
            }
        }
        parentJob.invokeOnCompletion { throwable ->
            if (throwable != null) {
                println("Parent job failed: $throwable")
            } else {
                println("Parent job SUCCESS")
            }
        }
    }

    private suspend fun getDouble(number: Int): Int {
        delay(500)
        return number * 2
    }

    private fun println(message: String) {
        Log.d(TAG, message)
    }
}