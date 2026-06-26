package com.example.mersyaapps.Home.pertemuan9

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mersyaapps.Home.pertemuan3.ThirdResultActivity
import com.example.mersyaapps.R
import com.example.mersyaapps.databinding.ActivityNinthBinding
import com.example.mersyaapps.utils.NotificationHelper
import com.example.mersyaapps.utils.PermissionHelper
import com.google.android.material.chip.Chip

class NinthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNinthBinding

    // 1. Daftarkan launcher permission di paling atas class
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Notifikasi diizinkan", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifikasi ditolak", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Menggunakan view binding secara eksklusif (Tanpa double setContentView)
        binding = ActivityNinthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 2. Minta izin pop-up saat halaman diakses
        checkNotificationPermission()

        // Setup Toolbar & Tombol Kembali berlambang ic_arrow_back
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Pertemuan 9"
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        // 3. Listener interaksi ChipGroupFilter
        binding.chipGroupFilter.setOnCheckedStateChangeListener { group, checkedIds ->
            val selectedChipId = checkedIds.firstOrNull()
            if (selectedChipId != null) {
                val chip = group.findViewById<Chip>(selectedChipId)
                val filterText = chip.text.toString()

                Toast.makeText(this, "Filter: $filterText", Toast.LENGTH_SHORT).show()

                // Memicu Local Notification via Global Helper
                val intent = Intent(this, ThirdResultActivity::class.java)
                NotificationHelper.showNotification(
                    this,
                    "Filter Diterapkan",
                    "Anda memilih kategori filter: $filterText",
                    intent
                )
            }
        }
    }

    // 4. Menangkap aksi klik tombol Navigasi Kembali (Panah Toolbar)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // Menutup Activity saat ini untuk kembali ke fragment home
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun checkNotificationPermission() {
        if (PermissionHelper.isNotificationPermissionRequired()) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (!PermissionHelper.hasPermission(this, permission)) {
                PermissionHelper.requestPermission(
                    notificationPermissionLauncher,
                    permission
                )
            }
        }
    }
}