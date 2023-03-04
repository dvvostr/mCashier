package ru.studiq.mcashier.UI.Activities

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuCompat
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import ru.studiq.mcashier.R
import ru.studiq.mcashier.UI.Activities.security.RegisterActivity
import ru.studiq.mcashier.UI.Activities.tools.CustomZXingScannerActivity
import ru.studiq.mcashier.common.Common
import ru.studiq.mcashier.interfaces.ICustomListActivityListener
import ru.studiq.mcashier.model.Settings
import ru.studiq.mcashier.model.classes.activities.common.CustomCompatActivity
import ru.studiq.mcashier.model.classes.hw.CustomHardwareError
import ru.studiq.mcashier.model.classes.hw.barcode.*
import ru.studiq.mcashier.model.classes.network.ProviderDataBody
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataDepartment
import java.io.Serializable

class SalesActivity : CustomCompatActivity() {
    private var allowManualInput: Boolean = Settings.Application.allowManualInputBarcode
    private var barcodeResultView: TextView? = null

    private var scanner: CustomBarcodeScanner? = null

//    private val barcodeLauncher = registerForActivityResult<ScanOptions, ScanIntentResult>(
//        ScanContract()
//    ) { result: ScanIntentResult ->
//        if (result.contents == null) {
//            val originalIntent = result.originalIntent
//            if (originalIntent == null) {
//                Log.d("MainActivity", "Cancelled scan")
//                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
//            } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
//                Log.d("MainActivity", "Cancelled scan due to missing camera permission")
//                Toast.makeText(this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show()
//            }
//        } else {
//            Log.d("MainActivity", "Scanned")
//            Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setupActivity() {
        super.setupActivity()
        setContentView(R.layout.activity_sales)
        supportActionBar?.title = getString(R.string.cap_sales).toUpperCase()
        supportActionBar?.subtitle = Settings.Application.currentDepartment?.caption
        barcodeResultView = findViewById(R.id.barcode_result_view)
        this.scanner = when (Settings.Application.deviceType) {
//            1 -> GoogleBarcodeScanner(this, GoogleBarcodeScannerOptions(Settings.Application.allowManualInputBarcode))
            1 -> SimpleBarcodeScanner(this, SimpleBarcodeScannerOptions())
            2 -> HoneywellBarcodeScanner(this, HoneywellBarcodeScannerOptions())
            else -> null
        }
        this.isLockBackNavigation = true
//        this.onBackPressedDispatcher.addCallback(this){
//            if (!isLockBackNavigation)
//                finish()
////            else
////                isLockBackNavigation = false
//        }
    }

//    override fun onBackPressed() {
//        if (!isLockBackNavigation)
//            super.onBackPressed()
//    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_sales, menu)
        if (menu != null) {
            MenuCompat.setGroupDividerEnabled(menu, true)
        }
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_sales_department -> {
                this.handleMenuDepartmentClick()
                true
            }
            else -> false
        }
        return super.onOptionsItemSelected(item)
    }
    private fun handleMenuDepartmentClick() {
        runOnUiThread {
            Common.WaitDialog.show(this, false)
        }
        DepartmentListActivity.Companion.load(this, object : ICustomListActivityListener {
            override fun onSuccess(sender: Context?, code: Int, msg: String, data: Serializable?) {
                try {
                    super.onSuccess(sender, code, msg, data)
                    val intent = Intent(sender, DepartmentListActivity::class.java).apply {
                        putExtra(Settings.Activities.ParentActivity, RegisterActivity::class.java.name)
                        putExtra(Settings.Activities.ActivityCaption, getString(R.string.cap_departments))
                        putExtra(Settings.Activities.ListItems, (data as? ProviderDataBody))
                    }
                    startActivityForResult(intent, RegisterActivity.DEPARTMENTLIST_ACTIVITY_REQUEST_CODE)
                } finally {
                    Common.WaitDialog.dismiss()
                }
            }
            override fun onError(sender: Context?, code: Int, msg: String, data: Serializable?) {
                Common.WaitDialog.dismiss()
                (sender as? RegisterActivity)?.let {
                    runOnUiThread {
                        Common.AlertDialog.show(it, getString(R.string.cap_error), msg, true)
                    }
                }
                super.onError(sender, code, msg, data)
            }
        })
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("RESULT", "Code ${requestCode}")
        if (resultCode == RESULT_OK && data != null) {
            val item: ProviderDataDepartment? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                data.getSerializableExtra(Settings.Extra.UserObject, ProviderDataDepartment::class.java) as? ProviderDataDepartment
            } else {
                data.getSerializableExtra(Settings.Extra.UserObject) as? ProviderDataDepartment
            }
            Settings.Application.currentDepartment = item
            supportActionBar?.subtitle = Settings.Application.currentDepartment?.caption ?: ""

        } else {
            // не удалось получить результат
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("RESULT", "Code ${requestCode}")
    }

    // ***** SCANNER ***** //
    fun onScanButtonClicked(view: View) {
        val options = ScanOptions()
////            .setOrientationLocked(false)
////            .setTimeout(8000)
//            .setCaptureActivity(
//                CustomZXingScannerActivity::class.java
//            )
//
//        barcodeLauncher.launch(options)
        scanner?.scan (object : IBarcodeReadListener {
            override fun onBarcodeRead(sender: CustomBarcodeScanner, barcode: String?) {
                barcodeResultView?.text = barcode ?: "NONE"
            }
            override fun onCancel(sender: CustomBarcodeScanner) {
                barcodeResultView?.text = "CANCEL"
            }
            override fun onError(sender: CustomBarcodeScanner, error: CustomHardwareError?) {
                barcodeResultView?.text = "${(error?.code ?: -1).toString()} : ${error?.text ?: "UNKNOWN"}"
            }
        })
    }


    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean(KEY_ALLOW_MANUAL_INPUT, allowManualInput)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        allowManualInput = savedInstanceState.getBoolean(KEY_ALLOW_MANUAL_INPUT)
    }

    companion object {
        private const val KEY_ALLOW_MANUAL_INPUT = "allow_manual_input"
    }
    // ******************* //
}