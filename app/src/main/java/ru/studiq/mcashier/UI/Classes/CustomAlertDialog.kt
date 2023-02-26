package ru.studiq.mcashier.UI.Classes

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.TextView
import androidx.cardview.widget.CardView
import ru.studiq.mcashier.R

class CustomAlertDialog {
    private var activity: Activity
    private var dialog: AlertDialog? = null
    private var cardView: CardView? = null
    private var labelCaption: TextView? = null
    private var labelDesc: TextView? = null

    constructor(activity: Activity) {
        this.activity = activity
    }

    @SuppressLint("MissingInflatedId")
    public fun show(caption: String, desc: String, canCancel: Boolean = false) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater: LayoutInflater = activity.layoutInflater
        val view = inflater.inflate(R.layout.custom_alert_dialog, null)
        builder.setView(view)
        builder.setCancelable(canCancel)

        this.cardView = view.findViewById(R.id.dialog_alert_cardview)
        this.labelCaption = view.findViewById(R.id.dialog_alert_caption)
        this.labelDesc = view.findViewById(R.id.dialog_alert_desc)
        this.labelCaption?.text = caption
        this.labelDesc?.text = desc
        this.dialog = builder.create()
        this.dialog?.window?.setBackgroundDrawableResource(R.color.clear);
        this.dialog?.show()
    }
    public fun dismiss() {
        this.dialog?.dismiss()
    }
}