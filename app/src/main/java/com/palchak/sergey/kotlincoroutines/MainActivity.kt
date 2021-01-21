package com.palchak.sergey.kotlincoroutines

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.palchak.sergey.kotlincoroutines.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

private const val PROGRESS_MAX = 100
private const val PROGRESS_START = 0
private const val JOB_TIME = 4000 // ms

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var job : CompletableJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.jobButton.setOnClickListener {
            if (!::job.isInitialized) initJob()
            binding.jobProgressBar.startOrCancel(job)
        }
    }

    private fun resetJob() {
        if (job.isActive || job.isCompleted) {
            job.cancel(CancellationException("Resetting job"))
        }
        initJob()
    }

    private fun initJob() {
        binding.jobButton.text = getString(R.string.start_job_1)
        updateJobCompleteTextView("")
        job = Job()
        job.invokeOnCompletion {
            val msg = it?.message ?: "Unknown error"
            showToast(msg)
        }
        binding.jobProgressBar.apply {
            max = PROGRESS_MAX
            progress = PROGRESS_START
        }
    }

    private fun showToast(text: String) {
        CoroutineScope(Main).launch {
            Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
        }
    }

    private fun ProgressBar.startOrCancel(job: Job) {
        if (this.progress > 0) {
            resetJob()
        } else {
            binding.jobButton.text = context.getString(R.string.cancel_job_1)
            CoroutineScope(IO + job).launch {
                for (i in PROGRESS_START..PROGRESS_MAX) {
                    delay((JOB_TIME / PROGRESS_MAX).toLong())
                    this@startOrCancel.progress = i
                }
                updateJobCompleteTextView("Job is complete!")
            }
        }
    }

    private fun updateJobCompleteTextView(text: String) {
        CoroutineScope(Main).launch {
            binding.jobCompleteText.text = text
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}