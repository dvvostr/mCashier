package ru.studiq.mcashier.model.classes.hw.barcode

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import com.journeyapps.barcodescanner.ScanOptions
import ru.studiq.mcashier.R
import ru.studiq.mcashier.model.classes.hw.CustomHardwareError

class GoogleBarcodeScannerOptions (manualInput: Boolean): CustomBarcodeScannerOptions() {
    val allowManualInput: Boolean = manualInput
}
class GoogleBarcodeScanner: CustomBarcodeScanner{
    constructor(context: Context, scannerOptions: CustomBarcodeScannerOptions): super(context, scannerOptions) {

    }

    override fun scan(listener: IBarcodeReadListener) {
        val activity: AppCompatActivity? = (scannerContext as? AppCompatActivity)?.let{ it }

        val optionsBuilder = GmsBarcodeScannerOptions.Builder()
        if ((options as? GoogleBarcodeScannerOptions)?.allowManualInput ?: false) {
            optionsBuilder.allowManualInput()
        }
        val gmsBarcodeScanner = activity?.let { GmsBarcodeScanning.getClient(it, optionsBuilder.build()) }
        gmsBarcodeScanner?.let {
            it.startScan()
            .addOnSuccessListener { barcode: Barcode ->
                try {
                    listener.onBarcodeRead(this, CustomBarcode(barcode.valueType.toString(), barcode.displayValue))
                } catch (ex: Exception) {
                    listener.onError(this, CustomHardwareError(-1, ex.message))
                }
            }
            .addOnFailureListener { ex: Exception ->
                val msg = (if (ex is MlKitException) {
                    when (ex.errorCode) {
                        MlKitException.CODE_SCANNER_CAMERA_PERMISSION_NOT_GRANTED ->
                            scannerContext?.getString(R.string.error_camera_permission_not_granted)
                        MlKitException.CODE_SCANNER_APP_NAME_UNAVAILABLE ->
                            scannerContext?.getString(R.string.error_app_name_unavailable)
                        else -> scannerContext?.getString(R.string.error_scanner_default_message, ex)
                    }
                } else {
                    ex.message
                })
                listener.onError(this, CustomHardwareError((ex as? MlKitException)?.errorCode ?: -1, msg))
            }
            .addOnCanceledListener {
                listener.onCancel(this)
            }
            .addOnCompleteListener {
//                mediaImage.close();
//                imageProxy.close();
            }

        }
    }
}