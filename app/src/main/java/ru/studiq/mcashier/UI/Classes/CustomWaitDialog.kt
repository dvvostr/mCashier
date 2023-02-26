package ru.studiq.mcashier.UI.Classes

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import ru.studiq.mcashier.R

class CustomWaitDialog {
    private var activity: Activity
    private var dialog: AlertDialog? = null

    constructor(activity: Activity) {
        this.activity = activity
    }

    public fun show(canCancel: Boolean = false) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_wait_dialog, null))
        builder.setCancelable(canCancel)
        this.dialog = builder.create()
        this.dialog?.window?.setBackgroundDrawableResource(R.color.clear);
        this.dialog?.show()
    }
    public fun dismiss() {
        this.dialog?.dismiss()
    }
}