package com.ssas.jibli.agent.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.zxing.BarcodeFormat
import com.google.zxing.ResultPoint
import com.google.zxing.client.android.BeepManager
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.base.BaseBindingActivity
import com.ssas.jibli.agent.data.constants.RequestCodes
import com.ssas.jibli.agent.data.constants.SharingKeys
import com.ssas.jibli.agent.databinding.ActivityBarCodeScannerBinding
import com.ssas.jibli.agent.utils.PermissionUtils

class BarCodeScannerActivity : BaseBindingActivity<ActivityBarCodeScannerBinding>(){

    private var barcodeScanner: DecoratedBarcodeView? = null
    private var formats: Collection<BarcodeFormat> = listOf(BarcodeFormat.CODE_128)
    private var beepManager: BeepManager? = null
    private var lastText: String? = null


    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_bar_code_scanner)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        changeStatusBarColor(R.color.colorPrimary)
        setListeners()
        setUpBarcodeView()
        setBipManager()
    }

    private fun setListeners(){
        binding.cancelScanBt.setOnClickListener {
            finish()
        }
    }

    private fun setUpBarcodeView() {
        barcodeScanner = findViewById(R.id.barcode_scanner) as? DecoratedBarcodeView
        barcodeScanner?.initializeFromIntent(intent)
        barcodeScanner?.decodeSingle(callback)
        barcodeScanner?.decoderFactory = DefaultDecoderFactory(formats)
    }

    private fun setBipManager() {
        beepManager = BeepManager(this)
    }

    private val callback: BarcodeCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult) {
            if (result.text == null || result.text == lastText) { // Prevent duplicate scans
                return
            }
            beepManager!!.playBeepSoundAndVibrate()
            lastText = result.text
            var intent = Intent()
            intent.putExtra(SharingKeys.QR_RESULT, result.text)
            setResult(RequestCodes.QR_RESULT_CODE, intent)
            finish()
        }

        override fun possibleResultPoints(resultPoints: List<ResultPoint>) {}
    }

    override fun onResume() {
        super.onResume()
        if (PermissionUtils.hasPermission(this, PermissionUtils.CAMERA_PERMISSION)) {
            barcodeScanner?.resume()
        }
    }

    override fun onStart() {
        super.onStart()
        if (PermissionUtils.hasPermission(this, PermissionUtils.CAMERA_PERMISSION)) {
            barcodeScanner?.resume()
        } else {
            PermissionUtils.requestPermissions(
                this,
                PermissionUtils.CAMERA_PERMISSION,
                PermissionUtils.PERMISSION_REQUEST_CAMERA_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionUtils.PERMISSION_REQUEST_CAMERA_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    barcodeScanner?.resume()
                } else {
                    alertDialogShow(this, getString(R.string.camera_permission_msg))
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        barcodeScanner?.pause()
    }
}