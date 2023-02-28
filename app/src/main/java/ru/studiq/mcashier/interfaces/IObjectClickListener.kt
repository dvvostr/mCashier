package ru.studiq.mcashier.interfaces

import android.util.Log
import android.view.View

typealias ObjectClickHandler = (view: View, data: Any?) -> Unit

interface IObjectClickListener {
    fun onObjectItemClick (view: View, data: Any?) {
    }
}