package ru.studiq.mcashier.UI.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.studiq.mcashier.R
import ru.studiq.mcashier.UI.Activities.security.RegisterActivity
import ru.studiq.mcashier.common.formatDouble
import ru.studiq.mcashier.model.Settings
import ru.studiq.mcashier.model.classes.activities.common.CustomCompatActivity
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderCheckoutDocument
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderCheckoutDocumentHeader
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataUser

class CartCheckoutActivity : CustomCompatActivity() {
    var successParentActivity: String = ""
    var failedParentActivity: String = ""

    private var textTotal: TextView? = null
    private var textQty: TextView? = null
    private var textStuff: TextView? = null
    private var textPaymentType: TextView? = null

    companion object {
        public const val ACTIVITY_REQUEST_CODE = 122
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
        val data = Gson().fromJson(str, ProviderCheckoutDocument::class.java)

        textTotal?.text = formatDouble(data.totalSum)
        textQty?.text = formatDouble(data.totalQty, "#,##0")
        textStuff?.text = data.staffs?.first()?.code ?: ""
        textPaymentType?.text = data.payment?.first()?.type ?: ""

        getSupportActionBar()?.let { actionBar ->
            actionBar.setDisplayHomeAsUpEnabled((successParentActivity.length > 0 && failedParentActivity.length > 0))
        }
    }

    fun onCheckoutButtonClicked(view: View) {
        finish()
    }
}