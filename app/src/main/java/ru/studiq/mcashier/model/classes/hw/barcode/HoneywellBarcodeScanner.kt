package ru.studiq.mcashier.model.classes.hw.barcode

import android.content.Context
import android.util.Log

class HoneywellBarcodeScannerOptions: CustomBarcodeScannerOptions() {

}
class HoneywellBarcodeScanner(context: Context, scannerOptions: HoneywellBarcodeScannerOptions): CustomBarcodeScanner(context, scannerOptions) {
    override fun scan(listener: IBarcodeReadListener) {
        Log.d("BARCODE", "Honeywell")
    }
}
