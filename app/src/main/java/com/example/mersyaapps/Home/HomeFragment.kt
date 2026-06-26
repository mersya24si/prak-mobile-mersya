package com.example.mersyaapps.Home

import android.Manifest
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mersyaapps.AuthActivity
import com.example.mersyaapps.Home.Pertemuan4.FourthActivity
import com.example.mersyaapps.Home.Pertemuan10.TenthActivity
import com.example.mersyaapps.Home.pertemuan3.ThirdActivity
import com.example.mersyaapps.Home.pertemuan3.ThirdResultActivity
import com.example.mersyaapps.Home.pertemuan_13.ThirteenthActivity
import com.example.mersyaapps.Home.pertemuan7.SeventhActivity
import com.example.mersyaapps.Home.pertemuan9.NinthActivity
import com.example.mersyaapps.Home.photo.PhotoAdapter
import com.example.mersyaapps.data.api.CatFactApiClient
import com.example.mersyaapps.data.api.PhotoApiClient
import com.example.mersyaapps.databinding.FragmentHomeBinding
import com.example.mersyaapps.utils.NotificationHelper
import com.example.mersyaapps.utils.PermissionHelper
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // 1. Launcher Permission ditaruh di sini agar aman dan mematuhi Lifecycle Fragment
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Izin notifikasi aktif!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Izin notifikasi ditolak!", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            title = "Home"
        }

        // 2. Minta izin pop-up runtime permission saat halaman utama diakses
        checkNotificationPermission()

        val sharedPref = requireContext().getSharedPreferences("user_pref", MODE_PRIVATE)

        // Pertemuan 4
        binding.button1.setOnClickListener {
            val intent = Intent(requireContext(), FourthActivity::class.java)
            intent.putExtra("nama", "Politeknik Caltex Riau")
            intent.putExtra("asal", "Rumbai")
            intent.putExtra("usia", 25)
            startActivity(intent)
        }

        loadCatFact()
        loadPhoto()

        // Pertemuan 7
        binding.button2.setOnClickListener {
            val intent = Intent(requireContext(), SeventhActivity::class.java)
            startActivity(intent)
        }

        // Pertemuan 9
        binding.button4.setOnClickListener {
            val intent = Intent(requireContext(), NinthActivity::class.java)
            startActivity(intent)
        }

        // Pertemuan 10
        binding.btnToTenth.setOnClickListener {
            val intent = Intent(requireContext(), TenthActivity::class.java)
            startActivity(intent)
        }

        // Pertemuan 13
        binding.btnToThirteenth.setOnClickListener {
            val intent = Intent(requireContext(), ThirteenthActivity::class.java)
            startActivity(intent)
        }

        // Pertemuan 3
        binding.button10.setOnClickListener {
            val intent = Intent(requireContext(), ThirdActivity::class.java)
            startActivity(intent)
        }

        // Logout
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Yakin ingin logout?")
                .setPositiveButton("Ya") { dialog, _ ->
                    val editor = sharedPref.edit()
                    editor.clear()
                    editor.apply()
                    dialog.dismiss()
                    startActivity(Intent(requireContext(), AuthActivity::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton("Tidak", null)
                .show()
        }

        // Tombol Refresh bawaan Anda
        binding.btnRefresh.setOnClickListener {
            loadCatFact()
        }
    }

    private fun loadCatFact() {
        lifecycleScope.launch {
            try {
                val response = CatFactApiClient.apiService.getCatFact()
                binding.tvCatFact.text = "\"${response.fact}\""

                // 3. SELESAI FETCH DATA: Picu notifikasi lokal menggunakan data dinamis dari API
                val intent = Intent(requireContext(), ThirdResultActivity::class.java)
                NotificationHelper.showNotification(
                    requireContext(),
                    "Fakta Kucing Diperbarui!",
                    "Fakta: ${response.fact}",
                    intent
                )

            } catch (e: Exception) {
                binding.tvCatFact.text = "Gagal mengambil fakta kucing."
            }
        }
    }

    private fun loadPhoto() {
        lifecycleScope.launch {
            try {
                val photos = PhotoApiClient.apiService.getPhotos()
                val adapter = PhotoAdapter(photos)
                binding.rvGallery.adapter = adapter
                binding.rvGallery.layoutManager = LinearLayoutManager(requireContext())
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal memuat gambar", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkNotificationPermission() {
        if (PermissionHelper.isNotificationPermissionRequired()) {
            val permission = Manifest.permission.POST_NOTIFICATIONS
            if (!PermissionHelper.hasPermission(requireContext(), permission)) {
                PermissionHelper.requestPermission(
                    notificationPermissionLauncher,
                    permission
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}