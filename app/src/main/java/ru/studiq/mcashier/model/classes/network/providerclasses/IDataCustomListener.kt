package ru.studiq.mcashier.model.classes.network.providerclasses

import android.content.Context

interface IDataCustomListener {
    abstract fun onSuccess(sender: Context?, code: Int, msg: String, data: Any?)
    open fun onEmpty(sender: Context?) {}
    open fun onError(sender: Context?, code: Int, msg: String) {}
}