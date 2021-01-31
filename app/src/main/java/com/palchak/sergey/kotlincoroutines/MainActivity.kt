package com.palchak.sergey.kotlincoroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.lang.Exception

private const val TAG = "AppDebug"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main()
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        println("Exception thrown in one of the children: $exception")
    }

    private fun main() {
        val parentJob = CoroutineScope(IO).launch() {

            supervisorScope {
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
                val jobB = launch(handler) {
                    val resultB = getDouble(2)
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
        delay(number * 500L)
        if (number == 2) throw Exception("Error getting result for number $number")
        return number * 2
    }

    private fun println(message: String) {
        Log.d(TAG, message)
    }
}