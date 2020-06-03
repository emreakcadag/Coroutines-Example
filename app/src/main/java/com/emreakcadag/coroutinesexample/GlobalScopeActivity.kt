package com.emreakcadag.coroutinesexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_glocal_scope.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GlobalScopeActivity : AppCompatActivity() {

    private lateinit var parentJob: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glocal_scope)

        title = TAG
        main()

        btn_global_scope.setOnClickListener {
            parentJob.cancel()
        }
    }

    private suspend fun work(i: Int) {
        delay(3000)

        log("Work $i is done. Thread: ${Thread.currentThread().name}")
    }

    private fun main() {

        log("Starting parent job...")
        val startTime = System.currentTimeMillis()

        parentJob = CoroutineScope(Main).launch {

            launch {
                work(1)
            }

            launch {
                work(2)
            }
        }

        parentJob.invokeOnCompletion {
            if (it != null) {
                log("Job was cancelled after ${System.currentTimeMillis() - startTime} ms.")
            } else {
                log("Done in ${System.currentTimeMillis() - startTime} ms.")
            }
        }
    }

    private fun log(message: String) {
        this.logMessage(message)
    }
}