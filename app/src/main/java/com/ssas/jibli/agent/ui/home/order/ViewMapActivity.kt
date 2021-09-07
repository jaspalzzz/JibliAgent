package com.ssas.jibli.agent.ui.home.order

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.base.BaseActivitySimple
import com.ssas.jibli.agent.data.constants.SharingKeys
import com.ssas.jibli.agent.data.models.merchantStore.SearchMerchantStoreArr
import com.ssas.jibli.agent.databinding.ActivityViewMapBinding
import com.ssas.jibli.agent.utils.PermissionUtils
import kotlin.properties.Delegates

class ViewMapActivity : BaseActivitySimple(), OnMapReadyCallback {
    lateinit var binding: ActivityViewMapBinding

    var locationLat: String = ""
    var locationLon: String = ""
    var deliveryAddress: String = ""
    var mGoogleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_map)
        handelIntent()
        initMap()
        initListener()
    }

    private fun handelIntent() {
        if (intent != null) {
            locationLat = intent.getStringExtra(SharingKeys.CUSTOMER_LAT) ?: ""
            locationLon = intent.getStringExtra(SharingKeys.CUSTOMER_LON) ?: ""
            deliveryAddress = intent.getStringExtra(SharingKeys.DELIVERY_ADDRESS) ?: ""
        }
    }

    private fun initListener() {
        binding.directionBt.setOnClickListener {

        }
        binding.cancelBt.setOnClickListener {
            finish()
        }
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        mGoogleMap = map
        map?.uiSettings?.isZoomControlsEnabled = true
        map?.uiSettings?.isMyLocationButtonEnabled = true
        map?.uiSettings?.isMapToolbarEnabled = true
        map?.uiSettings?.isMyLocationButtonEnabled= true

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            PermissionUtils.requestPermissions(
                this,
                PermissionUtils.LOCATION_PERMISSION,
                PermissionUtils.PERMISSION_LOCATION_REQUEST_CODE
            )
        } else {
            map?.isMyLocationEnabled = true
        }

        createShopMarker()
    }

    private fun createShopMarker() {
        val latLng = LatLng(locationLat.toDouble(), locationLon.toDouble())
        val markerOptions = MarkerOptions()
            .snippet(deliveryAddress)
            .position(latLng)
            .icon(BitmapDescriptorFactory.defaultMarker())
        mGoogleMap?.addMarker(markerOptions)
        mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionUtils.PERMISSION_LOCATION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mGoogleMap != null) {
                        mGoogleMap?.isMyLocationEnabled = true
                    }
                } else {
                    alertDialogShow(this, getString(R.string.location_permission_required))
                }
            }
        }
    }
}