package com.holmesye.logcollector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "holmesye"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: test123")
        Log.e("MainActivity", "onCreate: 123test")

//        var index = 1 / 0

        tvStop.setOnClickListener {
            var index = 1 / 0

        }
    }
}