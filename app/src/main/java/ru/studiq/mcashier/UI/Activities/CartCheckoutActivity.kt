package ru.studiq.mcashier.UI.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.studiq.mcashier.R
import ru.studiq.mcashier.model.classes.activities.common.CustomCompatActivity

class CartCheckoutActivity : CustomCompatActivity() {
    companion object {
        public const val ACTIVITY_REQUEST_CODE = 122
    }

    override fun setupActivity() {
        super.setupActivity()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_checkout)
    }
}