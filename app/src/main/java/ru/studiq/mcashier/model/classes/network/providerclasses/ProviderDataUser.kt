package ru.studiq.mcashier.model.classes.network.providerclasses

import com.google.gson.annotations.SerializedName

data class ProviderDataUser (
    @field:SerializedName("Id") val id: Int,
    @field:SerializedName("FIO") val userName: String,
    @field:SerializedName("StaffType") val stuffType: Int,
    @field:SerializedName("Status") val status: String,
    @field:SerializedName("StaffCode") val staffCode: String,
    @field:SerializedName("HRId") val hrID: Int,
    @field:SerializedName("DefDepName") val department: String,
    @field:SerializedName("Phone") val phone: String,
    @field:SerializedName("TabNumber") val tabNumber: Int
): java.io.Serializable {}
