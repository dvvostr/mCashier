package ru.studiq.mcashier.interfaces

import android.content.Context
import ru.studiq.mcashier.R
import ru.studiq.mcashier.model.classes.App
import java.io.Serializable

enum class CustomListActivityResult(val code: Int, val msg: String? = "") {
    success(0, "OK"),
    error(-1, App.res?.getString(R.string.error_unassigned) ?: "Error")
}

interface ICustomListActivityListener {
    fun onSuccess(sender: Context?, code: Int, msg: String, data: Serializable?) {}
    fun onEmpty(sender: Context?) {}
    fun onError(sender: Context?, code: Int, msg: String) {}
}