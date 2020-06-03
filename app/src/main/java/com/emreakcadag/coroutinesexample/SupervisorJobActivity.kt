package com.emreakcadag.coroutinesexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

class SupervisorJobActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_supervisor_job)

        title = TAG
        main()
    }

    private val handler = CoroutineExceptionHandler { _, exception ->
        log("Exception thrown somewhere within parent or child: $exception.")
    }

    private val childExceptionHandler = CoroutineExceptionHandler { _, exception ->
        log("Exception thrown in one of the children: $exception .")
    }

    private fun main() {

        val parentJob = CoroutineScope(Main).launch(handler) {

            supervisorScope { // *** Make sure to handle errors in children ***

                // --------- JOB A ---------
                val jobA = launch {
                    val resultA = getResult(1)
                    println("resultA: $resultA")
                }

                // --------- JOB B ---------
                val jobB = launch(childExceptionHandler) {
                    val resultB = getResult(2)
                    println("resultB: $resultB")
                }

                // --------- JOB C ---------
                val jobC = launch {
                    val resultC = getResult(3)
                    println("resultC: $resultC")
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

    private suspend fun getResult(number: Int): Int {
        return withContext(Main) {
            delay(number * 500L)
            if (number == 2) {
                throw Exception("Error getting result for number: $number")
            }
            number * 2
        }
    }

    private fun log(message: String) {
        this.logMessage(message)
    }
}
