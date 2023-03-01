package ru.studiq.mcashier.UI.Activities

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.MenuCompat
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import ru.studiq.mcashier.R
import ru.studiq.mcashier.UI.Activities.security.RegisterActivity
import ru.studiq.mcashier.common.Common
import ru.studiq.mcashier.interfaces.ICustomListActivityListener
import ru.studiq.mcashier.model.Settings
import ru.studiq.mcashier.model.classes.activities.common.CustomCompatActivity
import ru.studiq.mcashier.model.classes.network.ProviderDataBody
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataDepartment
import java.io.Serializable
import java.util.*

class SalesActivity : CustomCompatActivity() {
    private var allowManualInput: Boolean = true
    private var barcodeResultView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setupActivity() {
        super.setupActivity()
        setContentView(R.layout.activity_sales)
        supportActionBar?.title = getString(R.string.cap_sales).toUpperCase()
        supportActionBar?.subtitle = Settings.Application.currentDepartment?.caption

        barcodeResultView = findViewById(R.id.barcode_result_view)
    }
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
        val optionsBuilder = GmsBarcodeScannerOptions.Builder()
        if (allowManualInput) {
            optionsBuilder.allowManualInput()
        }
        val gmsBarcodeScanner = GmsBarcodeScanning.getClient(this, optionsBuilder.build())
        gmsBarcodeScanner
            .startScan()
            .addOnSuccessListener { barcode: Barcode ->
                try {
                    val obj = barcode
                    barcodeResultView?.text = obj.displayValue
//                    barcodeResultView!!.text = getSuccessfulMessage(barcode)
                } catch (ex: Exception) {
                    Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { e: Exception ->
                barcodeResultView!!.text = getErrorMessage(e)
            }
            .addOnCanceledListener {
                barcodeResultView!!.text = getString(R.string.error_scanner_cancelled)
            }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean(KEY_ALLOW_MANUAL_INPUT, allowManualInput)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        allowManualInput = savedInstanceState.getBoolean(KEY_ALLOW_MANUAL_INPUT)
    }

    private fun getSuccessfulMessage(barcode: Barcode): String {
        val barcodeValue =
            String.format(
                Locale.US,
                "Display Value: %s\nRaw Value: %s\nFormat: %s\nValue Type: %s",
                barcode.displayValue,
                barcode.rawValue,
                barcode.format,
                barcode.valueType
            )
        return barcodeValue
    }

    private fun getErrorMessage(e: Exception): String? {
        return if (e is MlKitException) {
            when (e.errorCode) {
                MlKitException.CODE_SCANNER_CAMERA_PERMISSION_NOT_GRANTED ->
                    getString(R.string.error_camera_permission_not_granted)
                MlKitException.CODE_SCANNER_APP_NAME_UNAVAILABLE ->
                    getString(R.string.error_app_name_unavailable)
                else -> getString(R.string.error_scanner_default_message, e)
            }
        } else {
            e.message
        }
    }

    companion object {
        private const val KEY_ALLOW_MANUAL_INPUT = "allow_manual_input"
    }
    // ******************* //
}