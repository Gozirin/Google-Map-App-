package com.example.googlemapwk8

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var map: GoogleMap
    private var myMarker: Marker? = null
    private lateinit var lastLocation: Location
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var ekeneMarker: Marker? = null

    // checking LOCATION_REQUEST_CODE
    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = getFusedLocationInstance(this)
    }
    private fun getFusedLocationInstance(context: Context): FusedLocationProviderClient {
        if (fusedLocationProviderClient == null) {
            fusedLocationProviderClient = FusedLocationProviderClient(context)
        }
        return fusedLocationProviderClient!!
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        setUpMap()
    }

    // SETTING UP THE CHECK SELF PERMISSION
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE
            )

            return
        }
        map.isMyLocationEnabled = true
        fusedLocationProviderClient!!.lastLocation.addOnSuccessListener(this) { location ->

            if (location != null) {
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLong)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
            }
            val ekeneLocation = LatLng(location.latitude, location.longitude)
            if (location != null) {
                lastLocation = location
                placePartnerMarkerOnMap(ekeneLocation)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(ekeneLocation, 12f))
            } else {
                ekeneMarker?.position = ekeneLocation
            }
        } // Sets the GooleMapOptions
        val options = GoogleMapOptions()
        options
            .compassEnabled(false)
            .rotateGesturesEnabled(false)
            .tiltGesturesEnabled(false)
    }

    // GET THE PLACE MARKER
    private fun placeMarkerOnMap(currentLatLng: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLng)
        markerOptions.title("Precious")
        map.addMarker(markerOptions)
    }
    private fun placePartnerMarkerOnMap(currentLatLng: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLng)
        markerOptions.title("Ekene")
        map.addMarker(markerOptions)
    }

    override fun onMarkerClick(p0: Marker) = false
}
