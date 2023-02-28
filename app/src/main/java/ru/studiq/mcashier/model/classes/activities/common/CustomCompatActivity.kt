package ru.studiq.mcashier.model.classes.activities.common

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import ru.studiq.mcashier.R
import ru.studiq.mcashier.UI.Activities.security.RegisterActivity
import ru.studiq.mcashier.model.Settings

open class CustomCompatActivity : AppCompatActivity() {
    public var parentActivityName: String = ""
    public var targetActivityName: String = ""
    private var actionbar: ActionBar? = null

    open var caption: String
        get() {
            return (this.actionbar?.title ?: "") as String
        }
        set(newValue) {
            this.actionbar?.title = newValue.toUpperCase()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setupActivity()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun finish() {
        super.finish()
// TODO Animate back transition
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
    // TODO Animate Ontions Item Click
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            android.R.id.home -> {
                super.onOptionsItemSelected(item)
            }
            else ->  {
                this.onMenuItemExecute(item)
                super.onOptionsItemSelected(item)
            }
        }
    }

    open fun setupActivity() {
        this.actionbar = this.supportActionBar
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setHomeAsUpIndicator(R.drawable.ic_chevron_thin_actionbar_left);
        actionbar?.displayOptions = androidx.appcompat.app.ActionBar.DISPLAY_SHOW_HOME or
                androidx.appcompat.app.ActionBar.DISPLAY_SHOW_TITLE or
                androidx.appcompat.app.ActionBar.DISPLAY_HOME_AS_UP or
                androidx.appcompat.app.ActionBar.DISPLAY_USE_LOGO
        intent.getStringExtra(Settings.Activities.ParentActivity)?.let { value ->
            this.parentActivityName = value
            getSupportActionBar()?.let { actionBar ->
                actionBar.setDisplayHomeAsUpEnabled(true)
            }
        }
        this.targetActivityName = intent.getStringExtra(Settings.Activities.TargetActivity) ?: ""
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
    open fun onMenuItemExecute(item: MenuItem) {

    }
    open fun setIcon(id: Int) {
        this.actionbar?.setIcon(id)
    }
}
