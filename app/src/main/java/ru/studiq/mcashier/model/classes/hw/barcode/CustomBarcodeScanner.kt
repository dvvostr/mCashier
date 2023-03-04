package ru.studiq.mcashier.model.classes.hw.barcode

import android.content.Context
import androidx.annotation.NonNull
import ru.studiq.mcashier.model.classes.hw.CustomHardwareError


interface IBarcodeReadListener {
    fun onBarcodeRead (sender: CustomBarcodeScanner, barcode: String?) {
    }
    fun onCancel (sender: CustomBarcodeScanner) {
    }
    fun onError (sender: CustomBarcodeScanner, error: CustomHardwareError?) {
    }
}
abstract class CustomBarcodeScannerOptions {
}

abstract class CustomBarcodeScanner() {
    private var intContext: Context? = null
    private var intOptions: CustomBarcodeScannerOptions? = null

    val scannerContext: Context?
    get() {
        return intContext
    }
    val scannerOptions: CustomBarcodeScannerOptions?
    get() {
        return intOptions
    }
    constructor(context: Context, scannerOptions: CustomBarcodeScannerOptions) : this() {
        this. intContext = context
        this.intOptions = options
        this.onCreate()
    }

    public var options: CustomBarcodeScannerOptions?
        get(){
            return intOptions
        }
        set(value) {
            intOptions = value
        }
    open fun onCreate() {}
    abstract fun scan(listener: IBarcodeReadListener)
}

//abstract class CustomBarcodeScanner(@NonNull context: Context, scannerOptions: CustomBarcodeScannerOptions) {
//
//    private var intOptions: CustomBarcodeScannerOptions = scannerOptions
//
//    public var options: CustomBarcodeScannerOptions
//    get(){
//        return intOptions
//    }
//    set(value) {
//        intOptions = value
//    }
//    abstract fun scan(listener: IBarcodeReadListener)
//}