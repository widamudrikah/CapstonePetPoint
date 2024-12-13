package com.capstone.petpoint.ui.detail

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.capstone.petpoint.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private var googleMap: GoogleMap? = null

    fun setLocation(latitude: Double, longitude: Double) {
        googleMap?.let {
            val latLng = LatLng(latitude, longitude)
            it.clear()
            it.addMarker(MarkerOptions().position(latLng).title("Pet Location"))
            it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        }
    }



    private val callback = OnMapReadyCallback { googleMap ->
        this.googleMap = googleMap
        val latitude = arguments?.getDouble("latitude")
        val longitude = arguments?.getDouble("longitude")
        if (latitude != null && longitude != null) {
            setLocation(latitude, longitude)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onDestroy() {
        super.onDestroy()
        googleMap?.clear() // Membersihkan data peta
        googleMap = null   // Melepaskan referensi GoogleMap
    }

}