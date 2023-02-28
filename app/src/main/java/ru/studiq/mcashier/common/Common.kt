package ru.studiq.mcashier.common

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import java.io.IOException
import ru.studiq.mcashier.R
import ru.studiq.mcashier.UI.Classes.CustomAlertDialog
import ru.studiq.mcashier.model.SettingData
import ru.studiq.mcashier.UI.Classes.CustomWaitDialog
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataMainMenuCard

typealias JSONText = String

object Common {
    var settings: SettingData? = null
    object const {
        val file_settings: String = "defaults.json"
    }
    val NUM_OF_COLUMN = 2

    fun initialize(context: Context) {
        try {
            val text = Common.getFileText(context, Common.const.file_settings)
            Common.settings = Gson().fromJson<SettingData>(text, SettingData::class.java)
        } catch (ex: Exception) {
            Common.settings = null
            ex.printStackTrace()
        }
    }

    fun getFileText(context: Context, file: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(file).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
    fun getMainMenuItems(context: Context): List<ProviderDataMainMenuCard> {
        val items: List<ProviderDataMainMenuCard>  = listOf(
            ProviderDataMainMenuCard(
                R.drawable.icon_posterminal,
                context.getString(R.string.cap_sales)
            ),
            ProviderDataMainMenuCard(
                R.drawable.icon_underconstruction,
                context.getString(R.string.cap_underconstruction)
            ),
            ProviderDataMainMenuCard(
                R.drawable.icon_underconstruction,
                context.getString(R.string.cap_underconstruction)
            ),
            ProviderDataMainMenuCard(
                R.drawable.icon_underconstruction,
                context.getString(R.string.cap_underconstruction)
            ),
            ProviderDataMainMenuCard(
                R.drawable.icon_underconstruction,
                context.getString(R.string.cap_underconstruction)
            ),
            ProviderDataMainMenuCard(
                R.drawable.icon_underconstruction,
                context.getString(R.string.cap_underconstruction)
            )
        )
        return items
    }
    object AlertDialog {
        private var dialog: CustomAlertDialog? = null

        fun show(activity: Activity, caption: String, desc: String, canCancel: Boolean = false) {
            if (dialog != null) {
                dialog?.dismiss()
            }
            dialog = CustomAlertDialog(activity)
            dialog?.show(caption, desc, canCancel)
        }
        fun dismiss() {
            dialog?.dismiss()
        }
    }
    object WaitDialog {
        private var waitDialog: CustomWaitDialog? = null

        fun show(activity: Activity, canCancel: Boolean = false) {
            if (WaitDialog.waitDialog != null) {
                WaitDialog.waitDialog?.dismiss()
            }
            WaitDialog.waitDialog = CustomWaitDialog(activity)
            WaitDialog.waitDialog?.show(canCancel)
        }
        fun dismiss() {
            WaitDialog.waitDialog?.dismiss()
        }
    }
}