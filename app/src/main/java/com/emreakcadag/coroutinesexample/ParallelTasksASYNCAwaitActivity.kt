package com.emreakcadag.coroutinesexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_parallel_tasks_async_await.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.system.measureTimeMillis

class ParallelTasksASYNCAwaitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parallel_tasks_async_await)

        title = TAG

        btn_start_parallel.setOnClickListener {
            setText("Clicked!")
            fakeApiRequest()
        }
    }

    private fun fakeApiRequest() {
        CoroutineScope(IO).launch {
            val executionTime = measureTimeMillis {
                val result1: Deferred<String?> = async {
                    log("launching job1 in thread: ${Thread.currentThread().name}")
                    getResult1FromApi()
                }

                val result2: Deferred<String?> = async {
                    log("launching job2 in thread: ${Thread.currentThread().name}")
                    getResult2FromApi()
                }

                setTextOnMainThread(result1.await())
                setTextOnMainThread(result2.await())
            }

            log("Total execution time in millis: $executionTime")
        }
    }

    private fun fakeApiRequestOld() {

        val startTime = System.currentTimeMillis()

        val parentJob = CoroutineScope(IO).launch {

            val job1 = launch {
                val time1 = measureTimeMillis {
                    log("launching job1 in thread: ${Thread.currentThread().name}")
                    val result1 = getResult1FromApi()
                    setTextOnMainThread(result1)
                }
                log("completed job1 in $time1 ms")
            }

            val job2 = launch {
                val time2 = measureTimeMillis {
                    log("launching job2 in thread: ${Thread.currentThread().name}")
                    val result2 = getResult2FromApi()
                    setTextOnMainThread(result2)
                }
                log("completed job2 in $time2 ms")
            }
        }

        parentJob.invokeOnCompletion {
            log("Total execution time in millis: ${System.currentTimeMillis() - startTime}")
        }
    }

    private fun setText(input: String) {
        tv_parallel.text = input
    }

    private suspend fun setTextOnMainThread(input: String?) {
        withContext(Dispatchers.Main) {
            tv_parallel.text = input
        }
    }

    private suspend fun getResult1FromApi(): String? {
        delay(1000)
        return RESULT_1
    }

    private suspend fun getResult2FromApi(): String? {
        delay(3000)
        return RESULT_2
    }

    private fun log(message: String) {
        this.logMessage(message)
    }
}