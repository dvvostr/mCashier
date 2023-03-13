package ru.studiq.mcashier.UI.Activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import ru.studiq.mcashier.R
import ru.studiq.mcashier.common.formatDouble
import ru.studiq.mcashier.model.Settings
import ru.studiq.mcashier.model.classes.activities.common.CustomCompatActivity
import ru.studiq.mcashier.model.classes.adapters.CartListAdapter
import ru.studiq.mcashier.model.classes.adapters.CartSwipeToDeleteCallback
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataDepartment
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataProductInfoExItems
import kotlin.math.abs
import kotlin.math.roundToInt

class CartActivity : CustomCompatActivity() {
    companion object {
        public const val CART_ACTIVITY_REQUEST_CODE = 121
    }

    private var textTotal: TextView? = null
    private var recyclerView: RecyclerView? = null
    private var items: ProviderDataProductInfoExItems = ProviderDataProductInfoExItems()

    private lateinit var swipeHelper: ItemTouchHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setupActivity() {
        super.setupActivity()
        setContentView(R.layout.activity_cart)
        textTotal = findViewById(R.id.cart_activity_text_total)
        recyclerView = findViewById(R.id.cart_activity_recyclerview)
        items = Gson().fromJson(
            intent.getStringExtra(Settings.Activities.ListJSON),
            ProviderDataProductInfoExItems::class.java
        )

        textTotal?.text = formatDouble(items.total)
        val adapter = items?.items?.let { CartListAdapter(this, it) }
        recyclerView?.adapter = adapter

        val swipeHandler = object : CartSwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView?.adapter as CartListAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                invalidate()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    override fun onBackPressed() {
        intent.putExtra(Settings.Extra.CartObject, Gson().toJson(items))
        setResult(RESULT_OK, intent)
        super.onBackPressed()
    }

    override fun invalidate() {
        super.invalidate()
        textTotal?.isVisible = (items.total > 0.0)
        textTotal?.text = formatDouble(items.total)
    }

    private val Int.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            toFloat(), resources.displayMetrics
        ).roundToInt()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("RESULT", "Code ${requestCode}")
        if (resultCode == RESULT_OK && data != null && resultCode == CartCheckoutActivity.ACTIVITY_REQUEST_CODE) {

        }
    }
    fun onCheckoutButtonClicked(view: View) {
        val data = items.asCheckoutDocument
        val intent = Intent(this, CartCheckoutActivity::class.java).apply {
            putExtra(Settings.Activities.ParentActivity, CartActivity::class.java.name)
            putExtra(Settings.Activities.ActivityCaption, getString(R.string.cap_checkout))
            putExtra(Settings.Activities.SuccessActivity, CartActivity::class.java.name)
            putExtra(Settings.Activities.FailedActivity, SalesActivity::class.java.name)
            putExtra(Settings.Activities.ListJSON, Gson().toJson(data))
        }
        startActivity(intent)
        finish()
    }
}