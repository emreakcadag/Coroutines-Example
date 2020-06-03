package com.emreakcadag.coroutinesexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

class RunBlockingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_run_blocking)

        title = TAG
        main()
    }

    private fun main() {
        runBlocking {

        }
        // job #1
        CoroutineScope(Main).launch {
            log("Starting job in thread: ${Thread.currentThread().name}")

            val result1 = getResult()
            log("result1: $result1")

            val result2 = getResult()
            log("result2: $result2")

            val result3 = getResult()
            log("result3: $result3")

            val result4 = getResult()
            log("result4: $result4")
        }

        // job #2
        CoroutineScope(Main).launch {
            delay(1000)
            runBlocking {
                log("Blocking thread: ${Thread.currentThread().name}")
                delay(4000)
                log("Done blocking thread: ${Thread.currentThread().name}")
            }
        }
    }

    private suspend fun getResult(): Int? {
        delay(1000)
        return Random.nextInt(100)
    }

    private fun log(message: String) {
        this.logMessage(message)
    }
}