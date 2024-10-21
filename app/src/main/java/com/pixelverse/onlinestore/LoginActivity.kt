package com.pixelverse.onlinestore

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    companion object{
        const val CLASS_NAME = "com.pixelverse.onlinestore.LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val botonLogueo: Button = findViewById<Button>(R.id.button)
        val botonRegistrarse: Button = findViewById<Button>(R.id.button2)

        botonRegistrarse.setOnClickListener {
            val intent = Intent(this, LoadingActivity::class.java)
            intent.putExtra("NEXT_ACTIVITY", RegisterActivity.CLASS_NAME)
            startActivity(intent)
        }

        botonLogueo.setOnClickListener{
            val intent = Intent(this, LoadingActivity::class.java)
            intent.putExtra("NEXT_ACTIVITY", ProductListActivity.CLASS_NAME)
            startActivity(intent)
        }
    }

}