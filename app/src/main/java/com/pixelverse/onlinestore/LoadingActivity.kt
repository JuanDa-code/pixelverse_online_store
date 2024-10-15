package com.pixelverse.onlinestore

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loading)

        verLoading()
    }

    private fun verLoading() {
        val nextActivity = intent.getStringExtra("NEXT_ACTIVITY") ?: ""

        Handler(Looper.getMainLooper()).postDelayed({
            val nextActivityClass = try {
                Class.forName(nextActivity)
            } catch (e: ClassNotFoundException) {
                Log.e("LoadingActivity", "Clase no encontrada: $nextActivity")
                null
            }

            if (nextActivityClass != null) {
                val intent = Intent(this, nextActivityClass)
                startActivity(intent)
            }

            finish()
        }, 3000)
    }
}