package ru.studiq.mcashier.model.classes.network.providerclasses

import ProviderDataProductDetail
import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import ru.studiq.mcashier.interfaces.ICustomListActivityListener
import ru.studiq.mcashier.model.classes.network.CreateProviderRequest
import ru.studiq.mcashier.model.classes.network.ProviderDataBody
import ru.studiq.mcashier.model.classes.network.ProviderRequestMethodStatic
import ru.studiq.mcashier.model.classes.network.ProviderRequestSystemType
import java.io.Serializable
import java.util.*

interface IDataProviderDataSaleDocumentListener: IDataCustomListener {
    fun onSuccess(sender: Context?, code: Int, msg: String, data: String?) {}
    override fun onSuccess(sender: Context?, code: Int, msg: String, data: Any?) {
        onSuccess(sender, code, msg, data as? String)
    }
}

data class ProviderDataSaleDocument (
    @field:SerializedName("DocumentNumber") val code: String,
    @field:SerializedName("Items") val items: List<ProviderDataProductDetail>?,
    @field:SerializedName("Rates") val currencyRate: List<ProviderDataCurrency>?

): java.io.Serializable {
    companion object {
    }
}

fun ProviderDataSaleDocument.Companion.load(sender: Context?, listener: IDataProviderDataSaleDocumentListener) {
    val request = CreateProviderRequest(UUID.randomUUID().toString(), ProviderRequestSystemType.cashrigester, ProviderRequestMethodStatic.none, "", "GenerateNewDocumentNumber")
    CustomProviderData.Companion.load(sender, request, listOf(), listOf(), object :
        ICustomListActivityListener {
        override fun onSuccess(sender: Context?, code: Int, msg: String, data: Serializable?) {
            super.onSuccess(sender, code, msg, data)
            val json = Gson().toJson((data as? ProviderDataBody)?.data)
            val out: List<ProviderDataSaleDocument?> = Gson().fromJson(Gson().toJson((data as? ProviderDataBody)?.data), object : TypeToken<List<ProviderDataSaleDocument?>?>() {}.type)
            listener.onSuccess(sender, code, msg, out?.first()?.code)
        }
        override fun onEmpty(sender: Context?) {
            super.onEmpty(sender)
            listener.onEmpty(sender)
        }
        override fun onError(sender: Context?, code: Int, msg: String) {
            super.onError(sender, code, msg)
            listener.onError(sender, code, msg)
        }
    })
}