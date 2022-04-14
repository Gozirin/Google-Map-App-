package com.example.googlemapwk8

import android.Manifest
import android.app.TaskStackBuilder.create
import android.content.IntentFilter.create
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase.create
import android.location.Location
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate.create
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.googlemapwk8.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationRequest.create
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.heartbeatinfo.HeartBeatConsumerComponent.create
import java.io.PushbackInputStream
import java.net.URI.create

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    companion object{
          private const val LOCATION_REQUEST_CODE = 1
      }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Set the uiSettings to be true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)

        setUpMap()

    }


       // SETTING UP THE CHECK SELF PERMISSION
    private fun setUpMap(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)

            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->

            if (location != null){
                lastLocation = location
                val currentLatLong = LatLng(location.latitude,location.longitude)
                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong,12f))

        }
        }

    }

      // GET THE PLACE MARKER
    private fun placeMarkerOnMap (currentLatLng: LatLng){
        val markerOptions = MarkerOptions ().position(currentLatLng)
        markerOptions.title("$currentLatLng")
        mMap.addMarker(markerOptions)


    }

    override fun onMarkerClick(p0: Marker)= false
}








//class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
//
//    private lateinit var locationRequest: LocationRequest
//    private lateinit var map: GoogleMap
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_maps)
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//        setupLocClient()
//
//    }
//
//    private lateinit var fusedLocClient: FusedLocationProviderClient
//    // use it to request location updates and get the latest location
//    override fun onMapReady(googleMap: GoogleMap) {
//        map = googleMap //initialise map
//        var myMap = map.addMarker(MarkerOptions().position(LatLng(0.0, 0.0)).title("You are currently here!"))
//        getCurrentLocation()
//    }
//    private fun setupLocClient() {
//        fusedLocClient =
//            LocationServices.getFusedLocationProviderClient(this)
//    }
//
//    // prompt the user to grant/deny access
//    private fun requestLocPermissions() {
//        ActivityCompat.requestPermissions(this,
//            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), //permission in the manifest
//            REQUEST_LOCATION)
//    }
//
//    companion object {
//        private const val REQUEST_LOCATION = 1 //request code to identify specific permission request
//        private const val TAG = "MapsActivity" // for debugging
//    }
//
//    private fun getCurrentLocation() {
//        // Check if the ACCESS_FINE_LOCATION permission was granted before requesting a location
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION) !=
//            PackageManager.PERMISSION_GRANTED) {
//
//            // call requestLocPermissions() if permission isn't granted
//            requestLocPermissions()
//        } else {
//            locationRequest = LocationRequest.a()
//            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//            val locationCallback = object : LocationCallback(){
//                override fun onLocationResult(p0: LocationResult) {
//                    getCurrentLocation()
//                }
//            }
//
//
//
//            fusedLocClient.requestLocationUpdates(locationRequest, locationCallback, mainLooper)
//            fusedLocClient.lastLocation.addOnCompleteListener {
//                // lastLocation is a task running in the background
//                val location = it.result //obtain location
//                //reference to the database
//                val database: FirebaseDatabase = FirebaseDatabase.getInstance()
//                val ref: DatabaseReference = database.getReference("test")
//                if (location != null) {
//
//                    val latLng = LatLng(location.latitude, location.longitude)
//                    Log.d(TAG, "getCurrentLocation: $latLng")
//                    // create a marker at the exact location
//                    map.addMarker(MarkerOptions().position(latLng)
//                        .title("You are currently here!"))
//                    // create an object that will specify how the camera will be updated
//                    val update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f)
//
//                    map.moveCamera(update)
//                    //Save the location data to the database
//                    ref.setValue(location)
//                } else {
//                    // if location is null , log an error message
//                    Log.e(TAG, "No location found")
//                }
//
//
//
//            }
//        }
//    }
//
//private fun FusedLocationProviderClient.requestLocationUpdates(locationRequest: LocationRequest, locationCallback: LocationCallback, mainLooper: Looper?) {
//
//}
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
////check if the request code matches the REQUEST_LOCATION if (requestCode == REQUEST_LOCATION)
//        {
////check if grantResults contains PERMISSION_GRANTED.If it does, call getCurrentLocation() if (grantResults.size == 1 && grantResults[0] ==
//            PackageManager.PERMISSION_GRANTED) {
//            getCurrentLocation()
//        } else {
////if it doesn`t log an error message Log.e(TAG, "Location permission has been denied")
//        // }
//        }
//    }
//
//
//
//}




