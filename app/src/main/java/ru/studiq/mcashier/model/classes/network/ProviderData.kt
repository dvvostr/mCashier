package ru.studiq.mcashier.model.classes.network

import com.google.gson.annotations.SerializedName

enum class ProviderDataBodyType {
    unassigned, normal, axapta
}

data class ProviderData(
    @field:SerializedName("header") val header: ProviderDataHeader,
    @field:SerializedName("body") val body: ProviderDataBody
) {}
data class ProviderDataHeader(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("code") val code: Int,
    @field:SerializedName("type") val type: Int,
    @field:SerializedName("method") val method: String,
    @field:SerializedName("msg") val msg: String,
    @field:SerializedName("desc") val desc: String
) { }
class ProviderDataBody(
    open @field:SerializedName("type") val type: Int,
    open @field:SerializedName("data") var data: Any?,
): java.io.Serializable {

}