package ru.studiq.mcashier.model.classes.network.providerclasses

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import ru.studiq.mcashier.common.formatDate
import ru.studiq.mcashier.interfaces.ICustomListActivityListener
import ru.studiq.mcashier.interfaces.IProviderClientListener
import ru.studiq.mcashier.model.classes.network.CreateProviderRequest
import ru.studiq.mcashier.model.classes.network.ProviderDataBody
import ru.studiq.mcashier.model.classes.network.ProviderRequestMethodStatic
import ru.studiq.mcashier.model.classes.network.ProviderRequestSystemType
import java.io.Serializable
import java.util.*

interface IDataProviderDataCurrencyListListener: IDataCustomListener {
    fun onSuccess(sender: Context?, code: Int, msg: String, data: ProviderDataCurrencyList?) {}
    override fun onSuccess(sender: Context?, code: Int, msg: String, data: Any?) {
        onSuccess(sender, code, msg, data as? ProviderDataCurrencyList)
    }
}

class ProviderDataCurrencyList() {
    constructor(list: List<ProviderDataCurrency>?) : this() {
        this.items = list
    }
    companion object {
        fun create(items: List<ProviderDataCurrency>?) {
            val obj = ProviderDataCurrencyList()
        }
    }
    public var items: List<ProviderDataCurrency>? = listOf()
    public fun item(index: Int): ProviderDataCurrency? {
        return this.items?.filter {
            it.id == index
        }?.first()
    }
}

data class ProviderDataCurrency (
    @field:SerializedName("InternalCurrencyID") val id: Int,
    @field:SerializedName("InternationalCurrencyCode") val code: Int,
    @field:SerializedName("Rate") val value: Float
): java.io.Serializable {
    companion object {
    }
}

fun ProviderDataCurrencyList.Companion.load(sender: Context?, listener: IDataProviderDataCurrencyListListener) {
    val request = CreateProviderRequest(UUID.randomUUID().toString(), ProviderRequestSystemType.cashrigester, ProviderRequestMethodStatic.none, "", "GetCashDeskRates")
    var params: List<String> = listOf()
    params = params.plus("<currenciesList><currencyRow currencyID = \"147\"/><currencyRow currencyID = \"148\"/></currenciesList>")
    params = params.plus(formatDate(Date(), "yyyyMMdd"))
    CustomProviderData.Companion.load(sender, request, listOf(), params, object :
        ICustomListActivityListener {
        override fun onSuccess(sender: Context?, code: Int, msg: String, data: Serializable?) {
            super.onSuccess(sender, code, msg, data)
            val json = Gson().toJson((data as? ProviderDataBody)?.data)
            var items = ProviderDataCurrencyList(Gson().fromJson(Gson().toJson((data as? ProviderDataBody)?.data), object : TypeToken<List<ProviderDataCurrency?>?>() {}.type))
            listener.onSuccess(sender, code, msg, items)
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