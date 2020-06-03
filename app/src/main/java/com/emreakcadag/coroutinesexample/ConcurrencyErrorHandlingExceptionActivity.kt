package com.emreakcadag.coroutinesexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class ConcurrencyErrorHandlingExceptionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concurrency_error_handling_exception)

        title = TAG
        main()
    }

    val handler = CoroutineExceptionHandler { _, exception ->
        println("Exception thrown in one of the children: $exception")
    }

    private fun main() {

        val parentJob = CoroutineScope(IO).launch(handler) {

            // --------- JOB A ---------
            val jobA = launch {
                val resultA = getResult(1)
                log("resultA: $resultA")
            }
            jobA.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    log("Error getting resultA: $throwable")
                }
            }

            // --------- JOB B ---------
            val jobB = launch {
                val resultB = getResult(2)
                log("resultB: $resultB")
            }
            jobB.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    log("Error getting resultB: $throwable")
                }
            }

            // --------- JOB C ---------
            val jobC = launch {
                val resultC = getResult(3)
                log("resultC: $resultC")
            }
            jobC.invokeOnCompletion { throwable ->
                if (throwable != null) {
                    log("Error getting resultC: $throwable")
                }
            }
        }

        parentJob.invokeOnCompletion { throwable ->
            if (throwable != null) {
                log("Parent job failed: $throwable")
            } else {
                log("Parent job SUCCESS")
            }
        }
    }

    private suspend fun getResult(number: Int) = withContext(Main) {
        delay(number * 500L)
        if (number == 2) {
            // throw Exception("Error getting result for number: ${number}")
            throw CancellationException("Error getting result for number: $number") // treated like "cancel()"
            // cancel(CancellationException("Error getting result for number: $number"))
        }
        number * 2
    }

    private fun log(message: String) {
        this.logMessage(message)
    }
}