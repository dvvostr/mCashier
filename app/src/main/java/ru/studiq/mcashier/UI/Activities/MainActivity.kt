package ru.studiq.mcashier.UI.Activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import ru.studiq.mcashier.R
import ru.studiq.mcashier.UI.Activities.security.UserListActivity
import ru.studiq.mcashier.common.Common
import ru.studiq.mcashier.interfaces.ICustomListActivityListener

fun MainActivity.Companion.Logon(sender: Context?, listener: ICustomListActivityListener) {
    val activity = sender as? Activity
    activity?.runOnUiThread {
        Common.WaitDialog.show(activity, false)
    }
    Handler().postDelayed({
        Common.WaitDialog.dismiss()
        listener.onSuccess(sender, 1, "OK", null)
    }, 5000)
}

class MainActivity : AppCompatActivity() {
    private var sendButton: AppCompatButton? = null

    companion object {
        val OBJ_USER_KEY = "USER_KEY"
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sendButton = findViewById(R.id.test_button_main)

        sendButton?.setOnClickListener {
            Toast.makeText(applicationContext, "Test button click", Toast.LENGTH_LONG).show()
            Log.d("Main activity", "Test button click")
//            val user = User(UserType.admin, "UID", "USER NAME", "URL")
//            val intent = Intent(this, PasscodeActivity::class.java)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//            intent.putExtra(OBJ_USER_KEY, user)
//            startActivity(intent)

        }
    }
}