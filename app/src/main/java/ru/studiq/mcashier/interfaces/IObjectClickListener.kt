package ru.studiq.mcashier.interfaces

import android.util.Log
import android.view.View

typealias ObjectClickHandler = (view: View, position: Int) -> Unit

interface IObjectClickListener {
    fun onCardItemClick (view: View, position: Int) {
    }
}