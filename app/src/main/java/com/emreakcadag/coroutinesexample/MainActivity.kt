package com.emreakcadag.coroutinesexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    companion object {
        private const val RESULT_1 = "Result #1"
        private const val RESULT_2 = "Result #2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_request_1.setOnClickListener {

            // Main IO default
            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }
    }

    private suspend fun fakeApiRequest() {
        val result1 = getResult1FromApi()
        Log.d("Emreeee", "$result1")
        setTextOnMainThread(result1)

        val result2 = getResult2FromApi()
        setTextOnMainThread(result2)
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
        Log.d("Emreeee", "$methodName: ${Thread.currentThread().name}")
    }

    private suspend fun setTextOnMainThread(input: String?) {
        withContext(Main) {
            textView.text = input
        }
    }
}