package com.pixelverse.onlinestore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    companion object{
        const val CLASS_NAME = "com.pixelverse.onlinestore.RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val botonLogin: Button = findViewById<Button>(R.id.button2)

        botonLogin.setOnClickListener {
            val intent = Intent(this, LoadingActivity::class.java)
            intent.putExtra("NEXT_ACTIVITY", LoginActivity.CLASS_NAME)
            startActivity(intent)
        }
    }
}