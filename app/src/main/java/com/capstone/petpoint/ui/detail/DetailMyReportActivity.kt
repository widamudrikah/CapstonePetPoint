package com.capstone.petpoint.ui.detail

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.capstone.petpoint.R
import com.capstone.petpoint.databinding.ActivityDetailMyReportBinding
import com.capstone.petpoint.response.DataItemDetail
import com.capstone.petpoint.response.DetailEmergencyUserResponse
import com.capstone.petpoint.utils.RetrofitInstance
import com.google.android.gms.maps.SupportMapFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailMyReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMyReportBinding
    private var Emergency: DataItemDetail? = null
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMyReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        Mengambil token
        token = getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            .getString("token", null)
        if (token.isNullOrEmpty()) {
            Log.e("DetailMyReportActivity", "Token tidak ditemukan")
        } else {
            Log.d("DetailMyReport", "Token berhasil didapatkan : $token")
        }

//        Mengambil ID Emergency
        val emergencyId = intent.getStringExtra("EMERGENCY_ID")
        if (emergencyId != null) {
            fetchEmergencyDetail(emergencyId)
        }

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        Memuat MapsFragment ke dalam FrameLayout
        if (savedInstanceState == null) {
            val mapsFragment = SupportMapFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .replace(R.id.map_container, mapsFragment)
                .commit()
        }
    }

    private fun fetchEmergencyDetail(emergencyId: String) {
        val client = RetrofitInstance.api.getDetailEmergencyUser("Bearer $token", emergencyId)
        client.enqueue(object : Callback<DetailEmergencyUserResponse> {
            override fun onResponse(call: Call<DetailEmergencyUserResponse>, response: Response<DetailEmergencyUserResponse>) {
                if (response.isSuccessful) {
                    val emergencyDetails = response.body()?.data

                    if (!emergencyDetails.isNullOrEmpty()) {
                        val emergencyDetail = emergencyDetails[0]
                        bindEmergencyDetail(emergencyDetail)
                    } else {
                        Log.e("DetailMyReportActivity", "Emergency dengan $emergencyId tidak ada")
                    }
                } else {
                    Log.e("DetailMyReportActivity", "Gagal mengambil data ID")
                }
            }

            override fun onFailure(call: Call<DetailEmergencyUserResponse>, t: Throwable) {
                Log.e("DetailMyReportActivity", "Message error: ${t.message}")
            }

        })
    }

    private fun bindEmergencyDetail(emergency: DataItemDetail) {
        binding.tvPetCategoryData.text = emergency.petCategory
        binding.tvAddressDesc.text = emergency.address
        binding.tvReportDate.text = emergency.dateStatus
        binding.tvReportHour.text = emergency.timeCreated
        binding.time1.text = emergency.timeCreated
        binding.time2.text = emergency.timeAccept ?: ""

        Glide.with(this)
            .load(emergency.picPet)
            .into(binding.imgPet)

        val timeAccept = emergency.timeAccept
        val timeEnd = emergency.timeEnd

        val textView = findViewById<TextView>(R.id.tv_finish)
        val motor = findViewById<ImageView>(R.id.icon_2)
        val yourReport = findViewById<TextView>(R.id.text_2)
        val line2 = findViewById<View>(R.id.line_2)


        if (timeAccept != null) {
            motor.visibility = View.VISIBLE
            yourReport.visibility = View.VISIBLE
            line2.visibility = View.GONE
            textView.visibility = View.VISIBLE
        } else {
            motor.visibility = View.GONE
            yourReport.visibility = View.GONE
            line2.visibility = View.GONE
            textView.visibility = View.GONE
        }

        if (timeEnd != null) {
            line2.visibility = View.VISIBLE
            textView.visibility = View.VISIBLE
        } else {
            line2.visibility = View.GONE
            textView.visibility = View.GONE
        }

//        lokasi
        val location = emergency.petLocation
//        memisahkan latitude dan longitude
        val locationParts = location?.split(",")
        if (locationParts?.size == 2) {
            val latitude = locationParts[0].toDoubleOrNull()
            val longitude = locationParts[1].toDoubleOrNull()

            Log.d("DetailMyReportActivity", "Latitude: $latitude, Logitude: $longitude")

            if (latitude != null && longitude != null) {
//                kirim data lokasi ke MapsFragment
                // Membuat MapsFragment baru dan mengirimkan data lokasi
                val mapFragment = MapsFragment()
                val bundle = Bundle()
                bundle.putDouble("latitude", latitude)
                bundle.putDouble("longitude", longitude)
                mapFragment.arguments = bundle

                // Menampilkan MapsFragment di dalam container
                supportFragmentManager.beginTransaction()
                    .replace(R.id.map_container, mapFragment)
                    .commit()
            } else {
                Log.e("DetailMyReportActivity", "Invalid location data")
            }
        } else {
            Log.e("DetailMyReportActivity", "Location format is invalid")
        }

    }
}