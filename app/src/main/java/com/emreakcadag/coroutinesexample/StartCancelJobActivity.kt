package com.emreakcadag.coroutinesexample

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start_cancel_job.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class StartCancelJobActivity : AppCompatActivity() {

    companion object {
        private const val PROGRESS_MAX = 100
        private const val PROGRESS_START = 0
        private const val JOB_TIME = 4000
    }

    private lateinit var job: CompletableJob

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_cancel_job)

        title = TAG

        btn_start_job.setOnClickListener {

            if (!::job.isInitialized) {
                initJob()
            }

            pb_main.startJobOrCancel(job)
        }
    }

    private fun initJob() {
        btn_start_job.text = "Start Job #1"
        updateJobCompleteTextView("")
        job = Job()
        job.invokeOnCompletion { e ->
            if (e?.message.isNullOrEmpty()) {
                val reason = "Unknown cancellation error"
                log("$job was cancelled. Reason: $reason")
                showToastt(reason)
            }
        }

        pb_main?.run {
            max = PROGRESS_MAX
            progress = PROGRESS_START
        }
    }

    private fun showToastt(info: String?) {
        GlobalScope.launch(Main) {
            // Check it!
            this@StartCancelJobActivity.showToast(info)
        }
    }

    private fun ProgressBar.startJobOrCancel(job: Job) {
        if (this.progress > 0) {
            log("$job is already active. Cancelling...")
            resetJob()
        } else {
            btn_start_job.text = "Cancel Job #1"
            CoroutineScope(IO + job).launch {
                log("coroutine $this is activated with job ${job}.")

                for (i in PROGRESS_START..PROGRESS_MAX) {
                    delay((JOB_TIME / PROGRESS_MAX).toLong())
                    this@startJobOrCancel.progress = i
                }
                updateJobCompleteTextView("Job is complete!")
            }
        }
    }

    private fun updateJobCompleteTextView(s: String) {
        GlobalScope.launch(Main) {
            tv_info.text = s
        }
    }

    private fun resetJob() {
        if (job.isActive or job.isCompleted) {
            job.cancel("Resetting job")
        }

        initJob()
    }

    private fun log(message: String) {
        this.logMessage(message)
    }
}