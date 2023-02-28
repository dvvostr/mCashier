package ru.studiq.mcashier.model.classes.network.providerclasses

import com.google.gson.annotations.SerializedName
data class ProviderDataMainMenuCard (
    @field:SerializedName("image") val image: Int,
    @field:SerializedName("caption") val caption: String,
    @field:SerializedName("description") val description: String = ""
): java.io.Serializable {
}
