package com.emreakcadag.coroutinesexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_freeze_ui.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FreezeUiActivity : AppCompatActivity() {

    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freeze_ui)

        title = TAG

        main()

        btn_freeze.setOnClickListener {
            tv_freeze.text = count++.toString()
        }
    }

    private suspend fun doNetworkRequest() {
        log("Starting network request...")
        delay(3000)
        log("Finished network request.")
    }

    private fun main() {
        CoroutineScope(Main).launch { // parent job
            log("Current")
            /*repeat(100000) { // 100,000 child job, this is freezing main thread
                launch {
                    doNetworkRequest()
                }
            }*/
            doNetworkRequest() // this is also freezing
        }
    }

    private fun log(message: String) {
        this.logMessage(message)
    }
}