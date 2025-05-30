package com.ebc.misfinanzas.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ebc.misfinanzas.R
import com.google.firebase.auth.FirebaseAuth

class DummyActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dummy)

        auth = FirebaseAuth.getInstance()

        val welcomeText = findViewById<TextView>(R.id.welcome_text)
        val registerButton = findViewById<Button>(R.id.register_movement_button)
        val logoutButton = findViewById<Button>(R.id.logout_button)

        val user = auth.currentUser
        welcomeText.text = "Bienvenido ${user?.email}"

        registerButton.setOnClickListener {
            val intent = Intent(this, MovimientoActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}

