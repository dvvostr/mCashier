package ru.studiq.mcashier.model.classes.hw.barcode

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import ru.studiq.mcashier.model.classes.hw.CustomHardwareError

class SimpleBarcodeScannerOptions (): CustomBarcodeScannerOptions() {
}
class SimpleBarcodeScanner: CustomBarcodeScanner {
    constructor(context: Context, scannerOptions: CustomBarcodeScannerOptions): super(context, scannerOptions) {

    }
    private var barcodeLauncher: ActivityResultLauncher<ScanOptions>? = null
    private var onScanListener: IBarcodeReadListener? = null

    override fun onCreate() {
        val activity: AppCompatActivity? = (scannerContext as? AppCompatActivity).let{ it }
        activity?.runOnUiThread {
            barcodeLauncher = activity?.registerForActivityResult<ScanOptions, ScanIntentResult>(
                ScanContract()
            ) { result: ScanIntentResult ->
                if (result.contents == null) {
                    val originalIntent = result.originalIntent
                    if (originalIntent == null) {
                        this.onScanListener?.onCancel(this)
                    } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        this.onScanListener?.onError(this, CustomHardwareError(-1, "Cancelled due to missing camera permission"))
                    }
                } else {
                    this.onScanListener?.onBarcodeRead(this, CustomBarcode(result.formatName, result.contents))
                }
            } //.also { barcodeLauncher =  it }
        }

    }

    override fun scan(listener: IBarcodeReadListener) {
        this.onScanListener = listener
        val options = ScanOptions()
        options.setTimeout(8000)
         barcodeLauncher?.launch(options)
    }
}