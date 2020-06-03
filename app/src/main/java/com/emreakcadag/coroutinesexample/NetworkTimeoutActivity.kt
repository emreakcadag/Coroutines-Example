package com.emreakcadag.coroutinesexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class NetworkTimeoutActivity : AppCompatActivity() {
    companion object {
        private const val TIMEOUT = 5900L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_timeout)

        title = TAG

        btn_request_1.setOnClickListener {

            // Main, IO, Default
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }
    }

    private suspend fun fakeApiRequest() {
        withContext(IO) {

            val job = withTimeoutOrNull(TIMEOUT) {
                val result1 = getResult1FromApi()
                log("result #1: $result1")
                setTextOnMainThread(result1)

                val result2 = getResult2FromApi()
                log("result #2: $result2")
                setTextOnMainThread(result2)
            }.takeIf { it == null }.run {
                log("TIMEOUT")
                setTextOnMainThread("timeout")
            }

        }
    }

    private suspend fun getResult1FromApi(): String? {
        logThread("getResult1FromApi")
        delay(3000)

        return RESULT_1
    }

    private suspend fun getResult2FromApi(): String? {
        logThread("getResult2FromApi")
        delay(3000)

        return RESULT_2
    }

    private suspend fun logThread(methodName: String) {
        Log.d(this@NetworkTimeoutActivity.TAG, "$methodName: ${Thread.currentThread().name}")
    }

    private suspend fun setTextOnMainThread(input: String?) {
        withContext(Dispatchers.Main) {
            textView.text = input
        }
    }

    private fun log(message: String) {
        this.logMessage(message)
    }
}