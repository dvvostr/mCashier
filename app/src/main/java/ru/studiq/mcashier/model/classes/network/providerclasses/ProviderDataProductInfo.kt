package ru.studiq.mcashier.model.classes.network.providerclasses

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import ru.studiq.mcashier.R
import ru.studiq.mcashier.interfaces.ICustomListActivityListener
import ru.studiq.mcashier.model.classes.network.*
import java.io.Serializable
import java.lang.Double.parseDouble

interface IDataProductInfoActivityListener {
    fun onSuccess(sender: Context?, code: Int, msg: String, data: ProviderDataProductInfo?) {}
    fun onEmpty(sender: Context?) {}
    fun onError(sender: Context?, code: Int, msg: String) {}
}
data class ProviderDataProductInfo(
    @field:SerializedName("PLU") val PLU: String,
    @field:SerializedName("Article") val article: String,
    @field:SerializedName("barcode") val barcode: String,
    @field:SerializedName("GTIN") val GTIN: String,
    @field:SerializedName("StockQty") val stockQty: Double?,
    @field:SerializedName("Price") val price: Double?,
    @field:SerializedName("PriceRUR") val priceRUR: Double?,
    @field:SerializedName("PriceEUR") val priceEUR: Double?,
    @field:SerializedName("MerchGroup") val merchGroup: String = "",
    @field:SerializedName("AdvActionsStr") val advActionsStr: String? = "",
    @field:SerializedName("MarkingTag") val markingTag: Int = 0,
    @field:SerializedName("VAT") val VAT: Double?,
    @field:SerializedName("TradeMark") val tradeMark: String = "",
    @field:SerializedName("SubTMName") val subTradeMarkName: String = "",
    @field:SerializedName("MarkingType") val markingType: Int?,
    @field:SerializedName("CarryOverTag") val carryOverTag: Int?,
    @field:SerializedName("PrincipalName") val principalName: String = "",
    @field:SerializedName("PrincipalPhone") val principalPhone: String = "",
    @field:SerializedName("PrincipalINN") val principalINN: String = "",
    @field:SerializedName("MerchType") val merchType: Int?
): CustomProviderData() {
    companion object {
    }
}

fun ProviderDataProductInfo.Companion.load(sender: Context?, params: String, listener: IDataProductInfoActivityListener) {
    // TODO проверить обработку ошибок при ошибке аксапта
    val request = CreateProviderRequest(
        java.util.UUID.randomUUID().toString(),
        ProviderRequestSystemType.axapta,
        ProviderRequestMethodStatic.none,
        "InventOnLineKassa_OnHand",
        "getinventOnHandToLocationIdExt"
    )
    val list = listOf(
        ru.studiq.mcashier.model.Settings.Application.currentDepartment?.id ?: "",
        params,
        "0",
        "",
        "",
        "@@"
    )
    CustomProviderData.Companion.load(sender, request, list, listOf(), object : ICustomListActivityListener {
        override fun onSuccess(sender: Context?, code: Int, msg: String, data: Serializable?) {
            super.onSuccess(sender, code, msg, data)
            val data = (data as? ProviderDataBody)?.let {
                val arr = (data.data as? String)?.split("@@")?.toTypedArray()
//                var json = "{" +
//                        "\"PLU\": \"${arr?.get(1) ?: ""}\", " +
//                        "\"Article\": \"${arr?.get(15) ?: ""}\", " +
//                        "\"barcode\": \"${arr?.get(2) ?: ""}\", " +
//                        "\"GTIN\": \"${arr?.get(18) ?: ""}\", " +
//                        "\"StockQty\": \"${qty}\", " +
//                        "\"Price\": \"${price1}\", " +
//                        "\"PriceRUR\": \"${price2}\", " +
//                        "\"PriceEUR\": \"${price3}\", " +
//                        "\"MerchGroup\": \"${if (arr?.size ?: 0 >= 10) arr?.get(9) ?: "" else arr?.get(7) ?: ""}\", " +
//                        "\"AdvActionsStr\": \"${""}\", " +
//                        "\"MarkingTag\": \"${if (arr?.size ?: 0 >= 13) arr?.get(12) else "0"}\", " +
//                        "\"VAT\": \"${vat}\", " +
//                        "\"TradeMark\": \"${arr?.get(14) ?: ""}\", " +
//                        "\"SubTMName\": \"${arr?.get(17) ?: ""}\", " +
//                        "\"MarkingType\": \"${arr?.get(19) ?: ""}\", " +
//                        "\"CarryOverTag\": \"${arr?.get(20) ?: ""}\", " +
//                        "\"PrincipalName\": \"${""}\", " +
//                        "\"PrincipalPhone\": \"${arr?.get(24) ?: ""}\", " +
//                        "\"PrincipalINN\": \"${arr?.get(23) ?: ""}\", " +
//                        "\"MerchType\": \"${arr?.get(23) ?: ""}\" " +
//                        "}"
////                val data = Gson().fromJson(json, ProviderDataProductInfo::class.java)
                if (arr?.size ?: 0 > 0 && arr?.get(0) as? String ?: "" == "OK"){
                    val qty = (arr?.get(3)?.replace(",", ".") ?: "").toString().replace(" ", "")
                    val price1 = (arr?.get(10)?.replace(",", ".") ?: "").toString().replace(" ", "")
                    val price2 = ((if (arr?.size ?: 0 >= 10) arr?.get(8) else arr?.get(4)) ?: "").replace(",", ".").toString().replace(" ", "")
                    val price3 = ((if (arr?.size ?: 0 >= 10) arr?.get(8) else arr?.get(4)) ?: "").replace(",", ".").toString().replace(" ", "")
                    val vat = (arr?.get(13)?.replace(",", ".") ?: "").toString().replace(" ", "")
                    val out = ProviderDataProductInfo(
                        arr?.get(1) ?: "",
                        arr?.get(15) ?: "",
                        arr?.get(2) ?: "",
                        arr?.get(18) ?: "", //*
                        qty.toDouble(),
                        price1.toDouble(),
                        price2.toDouble(),
                        price3.toDouble(),
                        "", if (arr?.size ?: 0 >= 10) arr?.get(9) ?: "" else arr?.get(7) ?: "",
                        if (arr?.size ?: 0 >= 13) arr?.get(12)?.toIntOrNull() ?: 0 else 0,
                        vat.toDouble(),
                        arr?.get(14) ?: "",
                        arr?.get(17) ?: "",
                        arr?.get(19)?.toIntOrNull(),
                        arr?.get(20)?.toIntOrNull(),
                        "",
                        arr?.get(24) ?: "",
                        arr?.get(23) ?: "",
                        arr?.get(23)?.toIntOrNull()
                    )
                    listener.onSuccess(sender, code, msg, out)

                } else {
                    listener.onError(sender, -1000, sender?.getString(R.string.err_data_empty) ?: "Data is empty")
                }
//                Log.d("DEBUG", str ?: "")
//                val json = Gson().toJson((data as? ProviderDataBody)?.data)
//                val list: List<ProviderDataProductInfo?> = Gson().fromJson(Gson().toJson((data as? ProviderDataBody)?.data), object : TypeToken<List<ProviderDataProductInfo?>?>() {}.type)
//                listener.onSuccess(sender, code, msg, list.first())
            } ?: run {
                listener.onError(sender, -1000, sender?.getString(R.string.error_unassigned) ?: "Unassigned error")
            }
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

