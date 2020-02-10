package com.example.googlemapexample

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

const val PERMISSION_REQUEST_FINE_LOCATION = 0

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.google_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {

            googleMap.isMyLocationEnabled = true

        } else {
            requestLocationPermission()
        }

        googleMap.uiSettings.isZoomControlsEnabled = true

        val newDelhi = LatLng(28.629717, 77.207065)
        googleMap.addMarker(
            MarkerOptions().position(newDelhi)
                .title("Marker at New Delhi")
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_person_pin_black_48dp))
                .icon(getBitmapDescriptorFromVector(this, R.drawable.ic_person_pin))
        )

//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(newDelhi))

        val zoomLevel = 10f
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newDelhi, zoomLevel))

        addMarkerOnLongClick()
    }


    private fun addMarkerOnLongClick(){
        map.setOnMapLongClickListener {
            val snippet = getString(R.string.new_marker, it.latitude, it.longitude
            )
            map.addMarker(
                MarkerOptions().position(it)
                    .title(getString(R.string.new_marker_title))
                    .snippet(snippet)
            )
        }
    }

    private fun  getBitmapDescriptorFromVector(context: Context, resourceId:Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(context, resourceId)
        vectorDrawable!!.setBounds(0, 0, vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight)
        val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas =  Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            val snack = Snackbar.make(google_map.requireView(), R.string.location_permission_rationale,
                Snackbar.LENGTH_INDEFINITE)
            snack.setAction(getString(R.string.ok)) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_FINE_LOCATION)
            }
            snack.show()
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_FINE_LOCATION) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                map.isMyLocationEnabled = true

            } else {
                Toast.makeText(this, getString(R.string.location_permission_denied),
                    Toast.LENGTH_SHORT). show()
            }
        }
    }

}
