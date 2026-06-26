package com.example.mersyaapps.Home.pertemuan3

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mersyaapps.R
import com.example.mersyaapps.databinding.ActivityThirdBinding
import com.example.mersyaapps.utils.NotificationHelper
import com.example.mersyaapps.utils.PermissionHelper

class ThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdBinding

    // 1. Menutup launcher dengan benar di sini }
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Notifikasi diizinkan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifikasi ditolak", Toast.LENGTH_SHORT).show()
            }
        }

    // 2. Fungsi onCreate sekarang berdiri sendiri dengan benar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        if (PermissionHelper.isNotificationPermissionRequired()) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (!PermissionHelper.hasPermission(this, permission)) {
                PermissionHelper.requestPermission(
                    notificationPermissionLauncher,
                    permission
                )
            }
        }

        binding.btnKirim.setOnClickListener {
            // Mengubah nama variabel dari 'nomor' ke 'noTujuan' agar sesuai dengan logikanya
            val noTujuan = binding.inputNoTujuan.text.toString()

            Log.e("Klik btnSubmit", "Tombol berhasil di tekan. Isi dari inputNama = $noTujuan")
            Toast.makeText(
                this,
                "Pesan ini berhasil dikirim ke : $noTujuan",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(this, ThirdResultActivity::class.java)
            NotificationHelper.showNotification(
                this,
                "Pesanan Anda",
                "Halo $noTujuan, Pesanan Anda Sedang Diproses",
                intent
            )

        }
    }
}