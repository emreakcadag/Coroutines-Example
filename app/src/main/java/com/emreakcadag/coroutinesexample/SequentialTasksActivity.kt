package com.emreakcadag.coroutinesexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sequential_tasks.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.system.measureTimeMillis

class SequentialTasksActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sequential_tasks)

        title = TAG

        btn_seq.setOnClickListener {
            setText("Clicked!")
            fakeApiRequest()
        }
    }

    private fun fakeApiRequest() {
        CoroutineScope(IO).launch {
            val executionTime = measureTimeMillis {
                log("launching job1: ${Thread.currentThread().name}")
                val result1 = getResult1FromApi()

                log("launching job2: ${Thread.currentThread().name}")

                val result2 = try {
                    getResult2FromApi(result1)
                } catch (e: CancellationException) {
                    log("Cancelled cause: $e")
                }

                log("Result2: $result2")
            }

            log("ExecutionTime in millis: $executionTime")
        }
    }

    private fun setText(input: String) {
        tv_seq.text = input
    }

    private suspend fun setTextOnMainThread(input: String?) {
        withContext(Dispatchers.Main) {
            tv_seq.text = input
        }
    }

    private suspend fun getResult1FromApi(): String? {
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(result1: String?): String? {
        delay(2000)
        if (result1 == RESULT_1) {
            setTextOnMainThread(RESULT_2)
            return RESULT_2
        } else {
            throw CancellationException("Result1 incorrect")
        }
    }

    private fun log(message: String) {
        this.logMessage(message)
    }
}