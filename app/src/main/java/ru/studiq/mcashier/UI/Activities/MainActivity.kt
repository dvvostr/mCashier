package ru.studiq.mcashier.UI.Activities

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.studiq.mcashier.R
import ru.studiq.mcashier.UI.Activities.security.RegisterActivity
import ru.studiq.mcashier.UI.Activities.security.UserListActivity
import ru.studiq.mcashier.UI.Activities.security.UserProfileActivity
import ru.studiq.mcashier.UI.Activities.security.load
import ru.studiq.mcashier.UI.Activities.tools.SetupActivity
import ru.studiq.mcashier.UI.Activities.tools.ToolsActivity
import ru.studiq.mcashier.common.Common
import ru.studiq.mcashier.common.SpacesItemDecoration
import ru.studiq.mcashier.interfaces.ICardItemClickListener
import ru.studiq.mcashier.interfaces.ICustomListActivityListener
import ru.studiq.mcashier.model.Settings
import ru.studiq.mcashier.model.classes.adapters.MainCardAdapter
import ru.studiq.mcashier.model.classes.network.ProviderDataBody
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataDepartment
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataMainMenuCard
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataUser
import java.io.Serializable


fun MainActivity.Companion.Logon(sender: Context?, listener: ICustomListActivityListener) {
    val activity = sender as? Activity
    activity?.runOnUiThread {
        Common.WaitDialog.show(activity, false)
    }
    Handler().postDelayed({
        Common.WaitDialog.dismiss()
        listener.onSuccess(sender, 1, "OK", null)
    }, 3000)
}

class MainActivity : AppCompatActivity() {
    private lateinit var items: List<ProviderDataMainMenuCard>
    private lateinit var cardList: RecyclerView
    private lateinit var adapter: MainCardAdapter
    private val REQUEST_ACCESS_TYPE = 1
    companion object {
        val OBJ_USER_KEY = "USER_KEY"
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = getString(R.string.app_name).uppercase()
        supportActionBar?.subtitle = Settings.Application.currentDepartment?.caption ?: "<${getString(R.string.error_department_not_set)}>"
        this.setupView()
    }
    @SuppressLint("ResourceAsColor")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu?.findItem(R.id.menu_main_caption)?.let {
            it.setTitle("%icon% ${Settings.Application.currentUser?.userName ?: getString(R.string.cap_notselected)}")
            val positionOfMenuItem = 0
            val s = SpannableString((Settings.Application.currentUser?.userName ?: getString(R.string.cap_notselected)).uppercase())
            s.setSpan(RelativeSizeSpan(1.1f), 0,s.length, 0)
            s.setSpan(ForegroundColorSpan(R.color.control_light), 0, s.length, 0)
            it.setTitle(s)
        }
        if (menu != null) {
            MenuCompat.setGroupDividerEnabled(menu, true)
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupView(){
        Common.initialize(this)
        this.setContentView(R.layout.activity_main)
        this.supportActionBar?.elevation = 0f
        this.load()
    }
    private fun load() {
        this.items = Common.getMainMenuItems(this)
        val activity = this
        cardList = this.findViewById(R.id.main_card_list) as RecyclerView
        if (cardList != null) {
            this.adapter = MainCardAdapter(this.items)
            this.adapter.cardItemClickEvent = object: ICardItemClickListener {
                override fun onCardItemClick(view: View, position: Int) {
                    val card = view.findViewById<CardView>(R.id.main_card_cardview)
                    if (card != null) {
                        val animator = ValueAnimator.ofInt(0, 1)
                        animator.duration = 100
                        animator.addUpdateListener { valueAnimator ->
                            val fractionAnim = valueAnimator.animatedValue as Int
                            if (valueAnimator.animatedValue as Int == 1) {
                                card.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.background_semi_light))
                            } else {
                                card.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.text_gray ))
                            }
                        }
                        animator.start()
                        handleCardItemClick(card, position)
                    }
                }
            }
            val layoutManager = GridLayoutManager(this, Common.NUM_OF_COLUMN)
            layoutManager.orientation = RecyclerView.VERTICAL
            layoutManager.spanSizeLookup = object: GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return 1
                }
            }
            cardList.layoutManager = layoutManager
            cardList.addItemDecoration(SpacesItemDecoration(0))
            cardList.adapter = this.adapter
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_caption -> {
                this.handleMenuCaptionClick()
                true
            }
            R.id.menu_main_department -> {
                this.handleMenuDepartmentClick()
                true
            }
            R.id.menu_main_exit -> {
                this.handleMenuLogoutClick()
                true
            }
            R.id.menu_main_tools -> {
                this.handleMenuToolClick()
                true
            }
            R.id.menu_main_settings -> {
                this.handleMenuSettingsClick()
                true
            }
            else -> false
        }
        return super.onOptionsItemSelected(item)
    }
    private fun handleMenuCaptionClick() {
        startActivity(Intent(this, UserProfileActivity::class.java))
    }
    private fun handleMenuDepartmentClick(){
        runOnUiThread {
            Common.WaitDialog.show(this, false)
        }
        DepartmentListActivity.Companion.load(this, object : ICustomListActivityListener {
            override fun onSuccess(sender: Context?, code: Int, msg: String, data: Serializable?) {
                try {
                    super.onSuccess(sender, code, msg, data)
                    val intent = Intent(sender, DepartmentListActivity::class.java).apply {
                        putExtra(Settings.Activities.ParentActivity, RegisterActivity::class.java.name)
                        putExtra(Settings.Activities.ActivityCaption, getString(R.string.cap_departments))
                        putExtra(Settings.Activities.ListItems, (data as? ProviderDataBody))
                    }
                    startActivityForResult(intent, RegisterActivity.DEPARTMENTLIST_ACTIVITY_REQUEST_CODE)
                } finally {
                    Common.WaitDialog.dismiss()
                }
            }
            override fun onError(sender: Context?, code: Int, msg: String, data: Serializable?) {
                Common.WaitDialog.dismiss()
                (sender as? RegisterActivity)?.let {
                    runOnUiThread {
                        Common.AlertDialog.show(it, getString(R.string.cap_error), msg, true)
                    }
                }
                super.onError(sender, code, msg, data)
            }
        })
    }
    private fun handleMenuLogoutClick() {
        val intent = Intent(this, RegisterActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
    private fun handleMenuToolClick() {
        startActivity(Intent(this, ToolsActivity::class.java))
    }
    private fun handleMenuSettingsClick() {
        startActivity(Intent(this, SetupActivity::class.java))
    }
    private fun handleCardItemClick(view: CardView, position: Int) {
        when (position) {
            0 -> if (Settings.Application.currentDepartment != null) {
                startActivity(Intent(this, SalesActivity::class.java))
            } else {
                Common.AlertDialog.show(this, getString(R.string.cap_error), getString(R.string.error_department_not_set), true)
            }
            else -> {
                Toast.makeText(applicationContext, "CardView ${position}", Toast.LENGTH_LONG).show()
                Handler().postDelayed({
                    Common.WaitDialog.dismiss()
                }, 5000)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("RESULT", "Code ${requestCode}")
        if (resultCode == RESULT_OK && data != null) {
            val item: ProviderDataDepartment? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                data.getSerializableExtra(Settings.Extra.UserObject, ProviderDataDepartment::class.java) as? ProviderDataDepartment
            } else {
                data.getSerializableExtra(Settings.Extra.UserObject) as? ProviderDataDepartment
            }
            Settings.Application.currentDepartment = item
            supportActionBar?.subtitle = Settings.Application.currentDepartment?.caption ?: ""

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
