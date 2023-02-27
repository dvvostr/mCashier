package ru.studiq.mcashier.UI.Activities.tools

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.studiq.mcashier.R
import ru.studiq.mcashier.model.classes.activities.common.CustomCompatActivity

class ToolsActivity : CustomCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tools)
        supportActionBar?.title = getString(R.string.cap_tools).uppercase()
    }
}