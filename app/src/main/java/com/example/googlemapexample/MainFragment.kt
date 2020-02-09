package com.example.googlemapexample


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar


/**
 * A simple [Fragment] subclass.
 */

const val PERMISSION_REQUEST_FINE_LOCATION = 0

class MainFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        mapView = view.findViewById(R.id.map_view)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {

        map = googleMap

        if (ActivityCompat.checkSelfPermission(activity as Context,
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
                .icon(getBitmapDescriptorFromVector(activity as Context, R.drawable.ic_person_pin))
        )

//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(newDelhi))

        val zoomLevel = 10f
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newDelhi, zoomLevel))
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
        val activity = activity as Activity
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            val snack = Snackbar.make(mapView, R.string.location_permission_rationale,
                Snackbar.LENGTH_INDEFINITE)
            snack.setAction(getString(R.string.ok)) {
                ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_FINE_LOCATION)
            }
            snack.show()
        } else {
            ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
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
                Toast.makeText(activity, getString(R.string.location_permission_denied),
                    Toast.LENGTH_SHORT). show()
            }
        }
    }

    override fun onStart() {
        mapView.onStart()
        super.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }


    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
