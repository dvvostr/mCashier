package ru.studiq.mcashier.UI.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import ru.studiq.mcashier.R
import ru.studiq.mcashier.common.Common
import ru.studiq.mcashier.common.formatDouble
import ru.studiq.mcashier.model.Settings
import ru.studiq.mcashier.model.classes.activities.common.CustomCompatActivity
import ru.studiq.mcashier.model.classes.network.providerclasses.IDataProviderCheckoutDocumentListener
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderCheckoutDocument

class CartCheckoutActivity : CustomCompatActivity() {
    var successParentActivity: String = ""
    var failedParentActivity: String = ""
    private var items: ProviderCheckoutDocument? = null

    private var textTotal: TextView? = null
    private var textQty: TextView? = null
    private var textStuff: TextView? = null
    private var textPaymentType: TextView? = null

    companion object {
        const val ACTIVITY_REQUEST_CODE = 122
        const val SUCCESS_CHECKOUT = "ru.studiq.mcashier.UI.Activities.SUCCESS_CHECKOUT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun setupActivity() {
        super.setupActivity()
        setContentView(R.layout.activity_cart_checkout)

        textTotal = findViewById(R.id.checkout_activity_action_total)
        textQty = findViewById(R.id.checkout_activity_value_qty)
        textStuff = findViewById(R.id.checkout_activity_value_stuff)
        textPaymentType = findViewById(R.id.checkout_activity_value_paymenttype)

        activity = this
        intent.getStringExtra(Settings.Activities.SuccessActivity)?.let { value ->
            this.successParentActivity = value
        }
        intent.getStringExtra(Settings.Activities.FailedActivity)?.let { value ->
            this.failedParentActivity = value
        }
        val str = intent.getStringExtra(Settings.Activities.ListJSON) ?: ""
        items = Gson().fromJson(str, ProviderCheckoutDocument::class.java)

        textTotal?.text = formatDouble(items?.totalSum ?: 0.0)
        textQty?.text = formatDouble(items?.totalQty ?: 0.0, "#,##0")
        textStuff?.text = items?.staffs?.first()?.code ?: ""
        textPaymentType?.text = items?.payment?.first()?.type ?: ""

        getSupportActionBar()?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled((successParentActivity.length > 0 && failedParentActivity.length > 0))
        }
    }

    fun onCheckoutButtonClicked(view: View) {
        val activity = this
        Common.WaitDialog.show(this, false)
        items?.save(this, object : IDataProviderCheckoutDocumentListener {
            override fun onSuccess(sender: Context?, code: Int, msg: String, data: Any?) {
                super.onSuccess(sender, code, msg, data)
                runOnUiThread {
                    Common.WaitDialog.dismiss()
                    val intent = Intent()
                    intent.putExtra(Settings.Extra.action, CartCheckoutActivity.Companion.ACTIVITY_REQUEST_CODE)
                    intent.putExtra(Settings.Extra.actionState, "OK")
                    setResult(RESULT_OK, intent)
                    finish()
                    true
                }
            }
            override fun onEmpty(sender: Context?) {
                super.onEmpty(sender)
                runOnUiThread {
                    Common.WaitDialog.dismiss()
                    Common.AlertDialog.show(activity, getString(R.string.cap_error), getString(R.string.err_data_empty))
                }
            }
            override fun onError(sender: Context?, code: Int, msg: String) {
                super.onError(sender, code, msg)
                runOnUiThread {
                    Common.WaitDialog.dismiss()
                    Common.AlertDialog.show(activity, getString(R.string.cap_error), msg)
                }
            }
        })
    }
}