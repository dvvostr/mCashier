package ru.studiq.mcashier.model


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import ru.studiq.mcashier.R
import ru.studiq.mcashier.model.classes.App
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataDepartment
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataUser

public final class Settings {
    companion object {
        public fun initialize() {
            Application.initialize()
            Storage.initialize()
            Activities.initialize()
            Extra.initialize()
        }
    }
    public final class Application {
        companion object {
            private var strUser: String = ""
            private var strDepartment: String = ""
            public var currentUser: ProviderDataUser?
            get() {
                return Gson().fromJson(strUser, ProviderDataUser::class.java)
            }
            set(value) {
                strUser = Gson().toJson(value)
                Storage.WriteStringKeyValue(App.appContext, Storage.LastUser, strUser)
            }
            public var currentDepartment: ProviderDataDepartment?
                get() {
                    return Gson().fromJson(strDepartment, ProviderDataDepartment::class.java)
                }
            set(value) {
                strDepartment = Gson().toJson(value)
                Storage.WriteStringKeyValue(App.appContext, Storage.LastDepartment, strDepartment)
            }
            public fun initialize() {
                Network.initialize()
                strUser = Storage.ReadStringKeyValue(App.appContext, Storage.LastUser) ?: ""
            }
        }
        public final class Network {
            companion object {
                val connectionURL: String
                get() { return Storage.connectionURL ?: "" }
                val connectionTimeout: Int?
                    get() { return Storage.connectionTimeout }
                val readTimeout: Int?
                    get() { return Storage.readTimeout }
                val writeTimeout: Int?
                    get() { return Storage.writeTimeout }
                public fun initialize() {
                }
            }
            fun a(): Int {
                return 1
            }
        }
    }
    public final class Storage {
        companion object {
            private val SharedPreferences = "SHARED_PREFERENCE_NAME"

            public fun initialize() {
            }
            val connectionURL: String?
                get() {
                    return App.instance?.let { value ->
                        getPreferenceString(value.getString(R.string.setting_id_connection))
                    }
                }
            val connectionTimeout: Int?
                get() {
                    return App.instance?.let { value ->
                        getPreferenceStringInteger(value.getString(R.string.setting_net_connect_timeout))
                    }
                }
            val readTimeout: Int?
                get() {
                    return App.instance?.let { value ->
                        getPreferenceStringInteger(value.getString(R.string.setting_net_read_timeout))
                    }
                }
            val writeTimeout: Int?
                get() {
                    return App.instance?.let { value ->
                        getPreferenceStringInteger(value.getString(R.string.setting_net_write_timeout))
                    }
                }
            val LastUser = "SETTING_LAST_USER_OBJECT"
            val LastUserIndex = "SETTING_LAST_USER_INDEX"
            val LastUserName = "SETTING_LAST_USER_NAME"
            val LastDepartment = "SETTING_LAST_DEPARTMENT"
            public fun getPreferenceString(key: String): String? {
                try {
                    return PreferenceManager.getDefaultSharedPreferences(App.appContext).getString(key, "")
                } catch (ex: Exception) {
                    return ""
                }
            }
            public fun getPreferenceStringInteger(key: String): Int? {
                try {
                    return PreferenceManager.getDefaultSharedPreferences(App.appContext).getString(key, "")?.toInt()
                } catch (ex: Exception) {
                    return null
                }
            }
            public fun getPreferenceInteger(key: String): Int? {
                try {
                    return PreferenceManager.getDefaultSharedPreferences(App.appContext).getInt(key, -1)
                } catch (ex: Exception) {
                    return null
                }
            }
            public fun getPreferenceString(context: Context, key: String): String? {
                return PreferenceManager.getDefaultSharedPreferences(context).getString(key, "")
            }
            public fun ReadStringKeyValue(context: Context, key: String): String? {
                val settings =  context.getSharedPreferences(Settings.Storage.SharedPreferences,Context.MODE_PRIVATE)
                return settings.getString(key, null)
            }
            public fun ReadIntKeyValue(context: Context, key: String): Int? {
                val settings =  context.getSharedPreferences(Settings.Storage.SharedPreferences,Context.MODE_PRIVATE)
                return settings.getInt(key, -1)
            }

            public fun WriteStringKeyValue(context: Context, key: String, value: String = "") {
                val settings =  context.getSharedPreferences(Settings.Storage.SharedPreferences,Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor = settings.edit();
                editor.putString(key, value);
                editor.apply();
            }
        }
    }
    public final class Activities {
        companion object {
            val ParentActivity: String = "PARENT_ACTIVITY"
            val TargetActivity: String = "TARGET_ACTIVITY"
            val ActivityCaption: String = "CAPTION_ACTIVITY"
            val ListItems: String = "LIST_DATA_ITEMS"
            public fun initialize() {
            }
        }
    }
    public final class Extra {
        companion object {
            val UserObject: String = "EXTRA_USER_OBJECT"
            public fun initialize() {
            }
        }
    }
}