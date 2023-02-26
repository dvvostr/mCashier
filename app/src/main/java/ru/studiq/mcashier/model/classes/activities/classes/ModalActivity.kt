package ru.studiq.mcashier.model.classes.activities.classes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.studiq.mcashier.UI.Activities.security.RegisterActivity
import ru.studiq.mcashier.model.Settings

open class ModalActivity: AppCompatActivity() {
    private var parentActivityName: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.getStringExtra(Settings.Activities.ParentActivity)?.let { value ->
            this.parentActivityName = value
            getSupportActionBar()?.let { actionBar ->
                actionBar.setDisplayHomeAsUpEnabled(true)
            }
        }
        intent.getStringExtra(Settings.Activities.ActivityCaption)?.let { value ->
            supportActionBar?.title = value.uppercase()
        }
    }

    override fun getSupportParentActivityIntent(): Intent? {
        return this.handleGetParentActivityIntent() ?: super.getSupportParentActivityIntent()
    }

    override fun getParentActivityIntent(): Intent? {
        return this.handleGetParentActivityIntent() ?: super.getParentActivityIntent()
    }

    private fun handleGetParentActivityIntent(): Intent? {
        var intent: Intent? = null
        RegisterActivity::class.java.name
        when (parentActivityName) {
            RegisterActivity::class.java.name ->
                intent = Intent(this, RegisterActivity::class.java)
            else -> {
            }
        }
        return intent
    }
}