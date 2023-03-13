package ru.studiq.mcashier.model.classes.network

import com.google.gson.annotations.SerializedName
import java.util.*

enum class ProviderRequestSystemType {
    unassigned, cashrigester, axapta
}
enum class ProviderRequestMethodStatic {
    none, static
}
public fun CreateProviderRequest(id: String, systemType: ProviderRequestSystemType, isStatic: ProviderRequestMethodStatic, className: String, methodName: String): ProviderRequest {
    return ProviderRequest(
        ProviderRequestHeader(id),
        ProviderRequestBody(systemType.ordinal, isStatic.ordinal, className, methodName, arrayOf<ProviderRequestMethodParam?>(), arrayOf<ProviderRequestMethodParam?>())
    )}
data class ProviderRequest(
    @field:SerializedName("header") val header: ProviderRequestHeader,
    @field:SerializedName("body") val body: ProviderRequestBody
) {}
data class ProviderRequestHeader(
    @field:SerializedName("id") var id: String
) {}
data class ProviderRequestBody(
    @field:SerializedName("SystemType") val type: Int,
    @field:SerializedName("StaticMethod") val isStatic: Int,
    @field:SerializedName("ClassName") val className: String,
    @field:SerializedName("MethodName") val methodName: String,

    @field:SerializedName("ConstructorParams") var constructorParams: Array<ProviderRequestMethodParam?>,
    @field:SerializedName("MethodParams") var methodParams: Array<ProviderRequestMethodParam?>,

//    @field:SerializedName("ConstructorParams") var constructorParams: Array<ProviderRequestMethodParam?>,
//    @field:SerializedName("MethodParams") var methodParams: Array<ProviderRequestMethodParam?>,
) {}
data class ProviderRequestMethodParam(
    @field:SerializedName("ParamValue") val value: String
) {}