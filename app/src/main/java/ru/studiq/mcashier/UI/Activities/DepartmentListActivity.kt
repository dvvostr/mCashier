package ru.studiq.mcashier.UI.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.studiq.mcashier.R
import ru.studiq.mcashier.model.classes.activities.common.CustomCompatActivity

class DepartmentListActivity : CustomCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_department_list)
    }

    override fun setupActivity() {
        super.setupActivity()
    }
}