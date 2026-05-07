package com.example.mersyaapps

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mersyaapps.databinding.ActivityAuthBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        //Kondisi jika isLogin bernilai true
//        val isLogin = sharedPref.getBoolean("isLogin", false)
//        if (isLogin) {
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()//kill auth activity
//        }
        binding.btnLogin.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            if (username == password && username.isNotEmpty() && password.isNotEmpty()) {
                sharedPref.edit {
                    putBoolean("isLogin", true)
                    putString("username", username)
                }
                Toast.makeText(this, " $username Login Anda Berhasil !", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("username", binding.username.text.toString()) // Kuncinya adalah "username"
                startActivity(intent)
            } else {5
                MaterialAlertDialogBuilder(this)
                    .setTitle("Coba Lagi")
                    .setMessage("Silahkan Coba lagi")
                    .setPositiveButton("Ya") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }
}