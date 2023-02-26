package ru.studiq.mcashier.UI.Activities.security

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import ru.studiq.mcashier.R
import ru.studiq.mcashier.UI.Classes.CustomListActivity
import ru.studiq.mcashier.model.classes.activities.classes.ModalActivity
import ru.studiq.mcashier.interfaces.ICustomListActivityListener
import ru.studiq.mcashier.interfaces.IProviderClientListener
import ru.studiq.mcashier.model.*
import ru.studiq.mcashier.model.classes.App
import ru.studiq.mcashier.model.classes.network.*
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataUser
import java.util.*

fun UserListActivity.Companion.load(sender: Context?, listener: ICustomListActivityListener) {
    val provider = ProviderClient()
    val id = java.util.UUID.randomUUID().toString()
    var url = Settings.Application.Network.connectionURL
    val paramValue = "'<elementsList><elementRow StaffTypeID = \"4\"/><elementRow StaffTypeID = \"5\"/></elementsList>'"
    val request = CreateProviderRequest(id, ProviderRequestSystemType.cashrigester, ProviderRequestMethodStatic.none, "", "GetStaffListRS")
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

class UserListActivity : CustomListActivity() {
    companion object {
    }

    private lateinit var listView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setupActivity() {
        super.setupActivity()
        setContentView(R.layout.activity_user_list)
        listView = findViewById(R.id.userlist_listview_userlist)
        var adapter = GroupAdapter<GroupieViewHolder>()
        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(Settings.Activities.ListItems, ProviderDataBody::class.java)
        } else {
            intent.getSerializableExtra(Settings.Activities.ListItems)
        }
        val list: List<ProviderDataUser?> = Gson().fromJson(Gson().toJson((data as? ProviderDataBody)?.data), object : TypeToken<List<ProviderDataUser?>?>() {}.type)
        list?.let { items ->
            items?.forEach {item ->
                val item: UserListItem? = item?.let {
                    UserListItem(it)
                }
                item?.let {
                    adapter.add(it)
                }
            }
        }
        adapter.setOnItemClickListener { item, view ->
            val userItem = item as UserListItem
            Settings.Storage.WriteStringKeyValue(this.applicationContext, Settings.Storage.LastUserName, userItem.user.userName)
            val intent = Intent()
            val user = userItem.user
            intent.putExtra(Settings.Extra.UserObject, user)
            setResult(RESULT_OK, intent)
            finish()
        }
        listView.adapter = adapter

    }
    class UserListItem(val user: ProviderDataUser): Item<GroupieViewHolder>() {
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            // val edUser =
            viewHolder.itemView.findViewById<TextView>(R.id.row_userlist_text_username).text = user.userName
            viewHolder.itemView.findViewById<TextView>(R.id.row_userlist_text_department).text = user.department
            viewHolder.itemView.findViewById<TextView>(R.id.row_userlist_text_stateType).text = user.status
            val edImage = viewHolder.itemView.findViewById<ImageView>(R.id.row_userlist_image_user)
//            Picasso.get().load(user.profileImageUrl).fit().centerCrop()
//                .placeholder(R.drawable.icon_user)
//                .error(R.drawable.icon_user)
//                .into(edImage);
        }

        override fun getLayout(): Int {
            return R.layout.activity_user_list_row_item
        }
    }
}

