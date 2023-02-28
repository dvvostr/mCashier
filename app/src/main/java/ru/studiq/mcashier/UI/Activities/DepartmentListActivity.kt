package ru.studiq.mcashier.UI.Activities

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap.Config
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.studiq.mcashier.R
import ru.studiq.mcashier.UI.Activities.security.UserListActivity
import ru.studiq.mcashier.common.Common
import ru.studiq.mcashier.common.SpacesItemDecoration
import ru.studiq.mcashier.interfaces.ICustomListActivityListener
import ru.studiq.mcashier.interfaces.IObjectClickListener
import ru.studiq.mcashier.interfaces.IProviderClientListener
import ru.studiq.mcashier.model.Settings
import ru.studiq.mcashier.model.classes.activities.common.CustomCompatActivity
import ru.studiq.mcashier.model.classes.adapters.DepartmentListAdapter
import ru.studiq.mcashier.model.classes.network.*
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataDepartment


fun DepartmentListActivity.Companion.load(sender: Context?, listener: ICustomListActivityListener) {
    val provider = ProviderClient()
    val id = java.util.UUID.randomUUID().toString()
    var url = Settings.Application.Network.connectionURL
    val paramValue = Settings.Application.currentUser?.staffCode ?: ""
    val request = CreateProviderRequest(id, ProviderRequestSystemType.cashrigester, ProviderRequestMethodStatic.none, "", "GetAllowedDepartmentsList")
    request.body.methodParams = request.body.methodParams.plus(ProviderRequestMethodParam(paramValue))
    try {
        provider.fetchJSON(
            url,
            Gson().toJson(request),
            object : IProviderClientListener {
                override fun onSuccess(
                    response: ProviderResponse,
                    header: ProviderDataHeader,
                    data: ProviderDataBody
                ) {
                    if (header.id == id && header.code >= 0 && data.type == ru.studiq.mcashier.model.classes.network.ProviderDataBodyType.normal.ordinal) {
                        listener.onSuccess(sender, header.code, header.msg, data)
                    } else {
                        listener.onError(sender, header.code, header.msg, null)
                    }
                }

                override fun onError(response: ProviderResponse, header: ProviderDataHeader) {
                    listener.onError(sender, header.code, header.msg, null)
                }
            }
        )
    } catch (ex: Exception) {
        listener.onError(sender, -1, ex.localizedMessage ?: sender?.getString(R.string.error_unassigned) ?: "", null)
    }
}
class DepartmentListActivity : CustomCompatActivity() {
    private lateinit var items: List<ProviderDataDepartment>
    private lateinit var userList: RecyclerView
    private lateinit var adapter: DepartmentListAdapter
    private val REQUEST_ACCESS_TYPE = 1
    companion object {
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun setupActivity() {
        super.setupActivity()
        setContentView(R.layout.activity_department_list)
        supportActionBar?.title = getString(R.string.cap_departments).uppercase()
//        supportActionBar?.subtitle = getString(R.string.cap_department_select)
        supportActionBar?.subtitle = Settings.Application.currentUser?.userName ?: ""
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(Settings.Activities.ListItems, ProviderDataBody::class.java)
        } else {
            intent.getSerializableExtra(Settings.Activities.ListItems)
        }
        load(data as? ProviderDataBody)
    }
    private fun load(data: ProviderDataBody?) {
        val type = object : TypeToken<List<ProviderDataDepartment?>?>() {}.type
        this.items =  Gson().fromJson(Gson().toJson(data?.data), type)

        val activity = this
        userList = this.findViewById(R.id.departmentlist_recyclerview) as RecyclerView
        if (userList != null) {
            this.adapter = DepartmentListAdapter(this.items)
            this.adapter.objectClickEvent = object: IObjectClickListener {
                override fun onObjectItemClick(view: View, data: Any?) {
                    (data as? ProviderDataDepartment)?.let { item ->
                        handleListObjectClick(item)
                    }
                }
            }
            val layoutManager = LinearLayoutManager(this)
            layoutManager.orientation = RecyclerView.VERTICAL
            userList.layoutManager = layoutManager
            userList.addItemDecoration(SpacesItemDecoration(0))
            userList.adapter = this.adapter
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
// TODO
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            else -> false
        }
        return super.onOptionsItemSelected(item)
    }
    private fun handleListObjectClick(item: ProviderDataDepartment) {
        Settings.Application.currentDepartment = item
        when (this.targetActivityName) {
            MainActivity::class.java.name -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK //.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                true
            }
            else -> {
                val intent = Intent()
                intent.putExtra(Settings.Extra.UserObject, item)
                setResult(RESULT_OK, intent)
                finish()
                true
            }
        }

    }
}