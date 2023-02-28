package ru.studiq.mcashier.UI.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.studiq.mcashier.R
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataDepartment

class DepartmentRowItemActivity : AppCompatActivity() {
    public var item: ProviderDataDepartment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_department_row_item)
    }
}