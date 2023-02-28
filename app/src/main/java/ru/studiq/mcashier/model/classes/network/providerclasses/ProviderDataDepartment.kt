package ru.studiq.mcashier.model.classes.network.providerclasses

import com.google.gson.annotations.SerializedName

class ProviderDataDepartment (
    @field:SerializedName("DepartmentID") val id: String,
    @field:SerializedName("DepartmentDescription") val caption: String,
    @field:SerializedName("ObjectName") val description: String
): java.io.Serializable { }