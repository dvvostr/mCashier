package ru.studiq.mcashier.UI.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.studiq.mcashier.R
import ru.studiq.mcashier.model.Settings
import ru.studiq.mcashier.model.classes.activities.common.CustomCompatActivity

class SalesActivity : CustomCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales)
    }

    override fun setupActivity() {
        super.setupActivity()
        supportActionBar?.title = getString(R.string.cap_sales).toUpperCase()
        supportActionBar?.subtitle = Settings.Application.currentDepartment?.departmentName
    }
}