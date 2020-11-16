package com.holmesye.logcollector

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "holmesye"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: 大厦上发的是打发士大夫")
        Log.e("MainActivity", "onCreate: error 大厦上发的是打发士大夫")
//        try {
//            var index = 1/0
//        }catch (e:ArithmeticException){
//            e.printStackTrace()
//        }
    }
}