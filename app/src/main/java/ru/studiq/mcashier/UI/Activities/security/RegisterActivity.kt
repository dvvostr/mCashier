package ru.studiq.mcashier.UI.Activities.security

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import ru.studiq.mcashier.R
import ru.studiq.mcashier.UI.Activities.DepartmentListActivity
import ru.studiq.mcashier.UI.Activities.Logon
import ru.studiq.mcashier.UI.Activities.MainActivity
import ru.studiq.mcashier.UI.Activities.load
import ru.studiq.mcashier.UI.Activities.tools.SetupActivity
import ru.studiq.mcashier.common.Common
import ru.studiq.mcashier.interfaces.ICustomListActivityListener
import ru.studiq.mcashier.model.Settings
import ru.studiq.mcashier.model.classes.network.*
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataUser
import java.io.Serializable


class RegisterActivity : AppCompatActivity() {
    companion object {
        public const val USERLIST_ACTIVITY_REQUEST_CODE = 1
        public const val DEPARTMENTLIST_ACTIVITY_REQUEST_CODE = 2
    }
    private lateinit var imageUser: ImageView
    private lateinit var edUserName: TextView
    private lateinit var btnBrowse: ImageButton
    private lateinit var btnEnter: AppCompatButton
    private lateinit var edPassword: EditText
    private lateinit var btnSettings: AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        imageUser = findViewById(R.id.register_imageuser)
        edUserName = findViewById(R.id.register_text_username)
        btnBrowse = findViewById(R.id.register_button_browse)
        btnEnter = findViewById(R.id.register_button_enter)
        edPassword = findViewById(R.id.register_text_password)
        btnSettings = findViewById(R.id.register_button_settings)

        edUserName.text = Settings.Application.currentUser?.userName ?: ""

        btnEnter.setOnClickListener { handleEnterClick() }
        btnBrowse.setOnClickListener { handleBrowseClick() }
        btnSettings.setOnClickListener { handleSettingsClick() }
    }
    private fun handleEnterClick() {
        if (Settings.Application.currentUser == null || Settings.Application.currentUser?.id ?: -1 <= 0) {
            Common.AlertDialog.show(this, getString(R.string.cap_error), getString(R.string.error_user_not_set), true)
            return
        } else {
            runOnUiThread {
                Common.WaitDialog.show(this, false)
            }
            DepartmentListActivity.Companion.load(this, object : ICustomListActivityListener {
                override fun onSuccess(sender: Context?, code: Int, msg: String, data: Serializable? ) {
                    try {
                        super.onSuccess(sender, code, msg, data)
                        val intent = Intent(sender, DepartmentListActivity::class.java).apply {
                            putExtra(Settings.Activities.ParentActivity, RegisterActivity::class.java.name)
                            putExtra(Settings.Activities.TargetActivity, MainActivity::class.java.name)
                            putExtra(Settings.Activities.ActivityCaption, getString(R.string.cap_departments))
                            putExtra(Settings.Activities.ListItems, (data as? ProviderDataBody))
                        }
                        startActivityForResult(intent, USERLIST_ACTIVITY_REQUEST_CODE)
                    } finally {
                        Common.WaitDialog.dismiss()
                    }
                }
                override fun onError(sender: Context?, code: Int, msg: String) {
                    Common.WaitDialog.dismiss()
                    (sender as? RegisterActivity)?.let {
                        runOnUiThread {
                            Common.AlertDialog.show(it, getString(R.string.cap_error), msg, true)
                        }
                    }
                    super.onError(sender, code, msg)
                }
            })
        }
    }
    private fun handleBrowseClick() {
        runOnUiThread {
            Common.WaitDialog.show(this, false)
        }
        UserListActivity.Companion.load(this, object : ICustomListActivityListener {
            override fun onSuccess(sender: Context?, code: Int, msg: String, data: Serializable?) {
                try {
                    super.onSuccess(sender, code, msg, data)
                    val intent = Intent(sender, UserListActivity::class.java).apply {
                        putExtra(Settings.Activities.ParentActivity, RegisterActivity::class.java.name)
                        putExtra(Settings.Activities.ActivityCaption, getString(R.string.cap_users))
                        putExtra(Settings.Activities.ListItems, (data as? ProviderDataBody))
                    }
                    startActivityForResult(intent, USERLIST_ACTIVITY_REQUEST_CODE)
                } finally {
                    Common.WaitDialog.dismiss()
                }
            }

            override fun onError(sender: Context?, code: Int, msg: String) {
                Common.WaitDialog.dismiss()
                (sender as? RegisterActivity)?.let {
                    runOnUiThread {
                        Common.AlertDialog.show(it, getString(R.string.cap_error), msg, true)
                    }
                }
                super.onError(sender, code, msg)
            }
        })
    }
    private fun handleSettingsClick() {
        val intent = Intent(this, SetupActivity::class.java)
        intent.putExtra(Settings.Activities.ParentActivity, RegisterActivity::class.java.name)
        intent.putExtra(Settings.Activities.ActivityCaption, getString(R.string.cap_settings))
        startActivity(intent)
    }

    private fun openUserList(values: ProviderDataBody) {
        val intent = Intent(this, UserListActivity::class.java)
        intent.putExtra(Settings.Activities.ParentActivity, RegisterActivity::class.java.name)
        intent.putExtra(Settings.Activities.ListItems, values)
        startActivityForResult(intent, USERLIST_ACTIVITY_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("RESULT", "Code ${requestCode}")
        if (resultCode == RESULT_OK && data != null) {
            val item: ProviderDataUser? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                data.getSerializableExtra(Settings.Extra.UserObject, ProviderDataUser::class.java) as? ProviderDataUser
            } else {
                data.getSerializableExtra(Settings.Extra.UserObject) as? ProviderDataUser
            }
            Settings.Application.currentUser = item
            edUserName.text = Settings.Application.currentUser?.userName ?: ""

        } else {
            // не удалось получить результат
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d("RESULT", "Code ${requestCode}")
    }
}