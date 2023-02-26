package ru.studiq.mcashier.model

import com.google.gson.annotations.SerializedName

data class SettingData(
    @SerializedName("common") val common: SettingsCommonData,
    @SerializedName("connections") val connectionList: ArrayList<SettingsCommonConnectionData>,
    @SerializedName("device-types") val deviceTypeList: ArrayList<SettingsCommonDeviceTypeData>
) {
    var activeConnection: SettingsCommonConnectionData? = null
        get() { return connectionList.filter { s -> s.id == common.connection }.single() }
    var activeDeviceType: SettingsCommonDeviceTypeData? = null
        get() { return deviceTypeList.filter { s -> s.id == common.deviceType }.single() }
}

data class SettingsCommonData (
    @SerializedName("last_user") val lastUser: SettingsCommonKeysData,
    @SerializedName("connection") val connection: Int,
    @SerializedName("device-type") val deviceType: Int,

    @SerializedName("RFID-mask") val rfidMask: String,
    @SerializedName("GTIN-check") val gtinCheck: Boolean,
    @SerializedName("SGTIN-filter") val gtinFilter: Boolean,
    @SerializedName("rfid-power") val rfidPower: Int,
    @SerializedName("isreader") val isReader: Boolean,
    @SerializedName("clear-type") val clearType: Int,
    @SerializedName("use-colorexport") val useColorExport: Boolean,
    @SerializedName("keys") val keys: ArrayList<SettingsCommonKeysData>,
)
data class SettingsCommonKeysData (
    @SerializedName("id") val id: Int,
    @SerializedName("value") val value: String
)
data class SettingsCommonConnectionData (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String,
    @SerializedName("profile") val profile: String,
    @SerializedName("desc") val desc: String
)
data class SettingsCommonDeviceTypeData (
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("desc") val desc: String
)
