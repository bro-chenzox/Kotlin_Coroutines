package com.palchak.sergey.kotlincoroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.palchak.sergey.kotlincoroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

private const val RESULT_1 = "Result #1"
private const val RESULT_2 = "Result #2"
private const val JOB_TIMEOUT = 1900L

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.button.setOnClickListener {
            setNewText("Click!")
            fakeApiRequest()
        }
    }

    private fun fakeApiRequest() {
        CoroutineScope(IO).launch {
            val job = withTimeoutOrNull(JOB_TIMEOUT) {
                val result1 = getResult1FromApi()
                println("debug: $result1")
                setTextonMainThread(result1)

                val result2 = getResult2FromApi()
                println("debug: $result2")
                setTextonMainThread(result2)
            }
            if (job == null) {
                val cancelMessage = "Cancelling Job...Job took longer than $JOB_TIMEOUT ms"
                println("debug: $cancelMessage")
                setTextonMainThread(cancelMessage)
            }
        }
    }

    private fun setNewText(input: String) {
        val newText = binding.text.text.toString() + "\n$input"
        binding.text.text = newText
    }

    private suspend fun setTextonMainThread(input: String) {
        withContext(Main) {
            setNewText(input)
        }
    }

    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(): String {
        logThread("getResult2FromApi")
        delay(1000)
        return RESULT_2
    }

    private fun logThread(methodName: String) {
        println("debug: $methodName: ${Thread.currentThread().name}")
    }
}