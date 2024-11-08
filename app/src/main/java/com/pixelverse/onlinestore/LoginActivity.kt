package com.pixelverse.onlinestore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.pixelverse.onlinestore.db.DbHelper
import org.mindrot.jbcrypt.BCrypt

class LoginActivity : AppCompatActivity() {
    companion object{
        const val CLASS_NAME = "com.pixelverse.onlinestore.LoginActivity"
    }

    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        dbHelper = DbHelper(this)
        val botonLogueo: Button = findViewById<Button>(R.id.button)
        val botonRegistrarse: Button = findViewById<Button>(R.id.button2)
        val usernameEditText = findViewById<EditText>(R.id.editTextText)
        val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword)

        botonRegistrarse.setOnClickListener {
            val intent = Intent(this, LoadingActivity::class.java)
            intent.putExtra("NEXT_ACTIVITY", RegisterActivity.CLASS_NAME)
            startActivity(intent)
        }

        botonLogueo.setOnClickListener{
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                val db = dbHelper.readableDatabase
                val cursor = db.query(
                    DbHelper.TABLE_USUARIOS,
                    arrayOf(DbHelper.COLUMN_CONTRASENA),
                    "${DbHelper.COLUMN_USERNAME} = ?",
                    arrayOf(username),
                    null,
                    null,
                    null
                )

                if (cursor.moveToFirst()) {
                    val hashedPassword = cursor.getString(0)
                    if (BCrypt.checkpw(password, hashedPassword)) {
                        val idUsuario = obtenerIdUsuario(username)

                        val sharedPref = getSharedPreferences("mis_preferencias", Context.MODE_PRIVATE)
                        with (sharedPref.edit()) {
                            putString("id_usuario", idUsuario)
                            apply()
                        }

                        Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoadingActivity::class.java)
                        intent.putExtra("NEXT_ACTIVITY", ProductListActivity.CLASS_NAME)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                }
                cursor.close()
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun obtenerIdUsuario(username: String): String {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT ${DbHelper.COLUMN_ID} FROM ${DbHelper.TABLE_USUARIOS} WHERE ${DbHelper.COLUMN_USERNAME} = ?", arrayOf(username))

        var idUsuario = ""
        if (cursor.moveToFirst()) {
            idUsuario = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ID))
        }
        cursor.close()
        return idUsuario
    }
}