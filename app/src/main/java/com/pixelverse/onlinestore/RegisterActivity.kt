package com.pixelverse.onlinestore

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.pixelverse.onlinestore.db.DbHelper
import org.mindrot.jbcrypt.BCrypt

class RegisterActivity : AppCompatActivity() {
    companion object{
        const val CLASS_NAME = "com.pixelverse.onlinestore.RegisterActivity"
    }

    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val botonLogin: Button = findViewById<Button>(R.id.button2)
        val botonRegistrarse: Button = findViewById<Button>(R.id.button)
        val usernameEditText = findViewById<EditText>(R.id.editTextText)
        val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword)
        dbHelper = DbHelper(this)

        botonLogin.setOnClickListener {
            val intent = Intent(this, LoadingActivity::class.java)
            intent.putExtra("NEXT_ACTIVITY", LoginActivity.CLASS_NAME)
            startActivity(intent)
        }

        botonRegistrarse.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())

                val values = ContentValues().apply {
                    put(DbHelper.COLUMN_USERNAME, username)
                    put(DbHelper.COLUMN_CONTRASENA, hashedPassword)
                }

                val db = dbHelper.writableDatabase

                val newRowId = db?.insert(DbHelper.TABLE_USUARIOS, null, values)

                if (newRowId != -1L) {
                    Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoadingActivity::class.java)
                    intent.putExtra("NEXT_ACTIVITY", LoginActivity.CLASS_NAME)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}