package ru.studiq.mcashier.UI.Activities.tools

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.ViewfinderView
import ru.studiq.mcashier.R
import java.util.*

class CustomZXingScannerActivity : AppCompatActivity() /*, DecoratedBarcodeView.TorchListener */{
    private val capture: CaptureManager? = null
    private var barcodeScannerView: DecoratedBarcodeView? = null
    private var switchFlashlightButton: Button? = null
    private var viewfinderView: ViewfinderView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_zxing_scanner)
//        barcodeScannerView = findViewById(R.id.zxing_barcode_scanner)
//        barcodeScannerView?.setTorchListener(this)
//
//        switchFlashlightButton = findViewById(R.id.switch_flashlight)

//        viewfinderView = findViewById(R.id.zxing_viewfinder_view)
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        return super.onTouchEvent(event)
//    }
//override fun onTorchOn() {
////        switchFlashlightButton.setText(R.string.turn_off_flashlight)
//}

//    override fun onTorchOff() {
////        switchFlashlightButton.setText(R.string.turn_on_flashlight)
//    }

    override fun onResume() {
        super.onResume()
        capture!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        capture!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        capture!!.onDestroy()
    }

//    protected fun onSaveInstanceState(outState: Bundle?) {
//        super.onSaveInstanceState(outState!!)
//        capture!!.onSaveInstanceState(outState)
//    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        return barcodeScannerView!!.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
//    }
//
    /**
     * Check if the device's camera has a Flashlight.
     * @return true if there is Flashlight, otherwise false.
     */
//    private fun hasFlash(): Boolean {
//        return applicationContext.packageManager
//            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
//    }
//
//    fun switchFlashlight(view: View?) {
//        if (getString(R.string.turn_on_flashlight) == switchFlashlightButton!!.text) {
//            barcodeScannerView!!.setTorchOn()
//        } else {
//            barcodeScannerView!!.setTorchOff()
//        }
//    }
//
//    fun changeMaskColor(view: View?) {
//        val rnd = Random()
//        val color = Color.argb(100, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
//        viewfinderView!!.setMaskColor(color)
//    }
//
//    fun changeLaserVisibility(visible: Boolean) {
//        viewfinderView!!.setLaserVisibility(visible)
//    }
//

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        capture!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
}