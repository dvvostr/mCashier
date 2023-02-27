package ru.studiq.mcashier.interfaces

import android.util.Log
import android.view.View

typealias CardItemClickHandler = (view: View, position: Int) -> Unit

interface ICardItemClickListener {
    fun onCardItemClick (view: View, position: Int) {
    }
}