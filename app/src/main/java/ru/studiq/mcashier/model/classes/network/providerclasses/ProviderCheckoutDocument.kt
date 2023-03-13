package ru.studiq.mcashier.model.classes.network.providerclasses

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import ru.studiq.mcashier.common.formatDate
import ru.studiq.mcashier.interfaces.ICustomListActivityListener
import ru.studiq.mcashier.model.classes.network.CreateProviderRequest
import ru.studiq.mcashier.model.classes.network.ProviderDataBody
import ru.studiq.mcashier.model.classes.network.ProviderRequestMethodStatic
import ru.studiq.mcashier.model.classes.network.ProviderRequestSystemType
import java.io.Serializable
import java.util.*

interface IDataProviderCheckoutDocumentListener: IDataCustomListener {
    fun onSuccess(sender: Context?, code: Int, msg: String, data: String?) {}
    override fun onSuccess(sender: Context?, code: Int, msg: String, data: Any?) {
        onSuccess(sender, code, msg, data as? String)
    }
}
data class ProviderCheckoutDocumentHeader(
    @field:SerializedName("DocumentNumber") var code: String,
    @field:SerializedName("DocumentDate") var strDate: String,
    @field:SerializedName("InventLocationId") var location: String,
    @field:SerializedName("CashBoxID") var cashbox: String,
    @field:SerializedName("CRMClientID") var CRMClientID: String,
    @field:SerializedName("CreatorCode") var creator: String
): CustomProviderData() {
    companion object {
    }
}
data class ProviderCheckoutDocumentArticle(
    @field:SerializedName("BarCode") var barcode: String,
    @field:SerializedName("ItemIndex") var posIndex: Int,
    @field:SerializedName("Quantity") var qty: Double,
    @field:SerializedName("Price") var price: Double,
    @field:SerializedName("FullPrice") var fullPrice: Double,
    @field:SerializedName("OriginalPrice") var originalPrice: Double,
    @field:SerializedName("VAT") var VAT: Double,
    @field:SerializedName("Category") var category: String
): CustomProviderData() {
    companion object {
    }
}
data class ProviderCheckoutDocumentStaff (
    @field:SerializedName("LineIndex") var lineIndex: Int,
    @field:SerializedName("Cashier") var isCashier: Int,
    @field:SerializedName("EmployeeID") var id: Int,
    @field:SerializedName("EmployeeCode") var code: String,
    @field:SerializedName("EmployeeName") var name: String,
    @field:SerializedName("AmountShare") var amountShare: Double,
    @field:SerializedName("PercentShare") var percentShare: Int
): CustomProviderData() {
    companion object {
    }
}

data class ProviderCheckoutDocumentPayment(
    @field:SerializedName("PaymentType") var type: String,
    @field:SerializedName("PaymentAmount") var value: Double
): CustomProviderData() {
    companion object {
    }
}
data class ProviderCheckoutDocumentReservesList(
    @field:SerializedName("Code") var code: String,
    @field:SerializedName("Value") var value: String
): CustomProviderData() {
    companion object {
    }
}
data class ProviderCheckoutDocument (
    @field:SerializedName("OperationType") var operationType: Int,
    @field:SerializedName("DocumentHeader") var header: ProviderCheckoutDocumentHeader?,
    @field:SerializedName("Articles") var articles: List<ProviderCheckoutDocumentArticle>?,
    @field:SerializedName("staffList") var staffs: List<ProviderCheckoutDocumentStaff>?,
    @field:SerializedName("PaymentsList") var payment: List<ProviderCheckoutDocumentPayment>?,
    @field:SerializedName("ReservesList") var peservesList: List<ProviderCheckoutDocumentReservesList>?
): CustomProviderData() {
    companion object {
    }
    public var totalSum: Double = 0.0
        get(){
        var value: Double = 0.0
        this.articles?.forEach({
            value = value.plus(it.qty * it.price)
        })
        return value
    }
    public var totalQty: Double = 0.0
        get(){
            var value: Double = 0.0
            this.articles?.forEach({
                value = value.plus(it.qty)
            })
            return value
        }
    fun save(sender: Context?, listener: IDataProviderCheckoutDocumentListener) {
        val request = CreateProviderRequest(UUID.randomUUID().toString(), ProviderRequestSystemType.axapta, ProviderRequestMethodStatic.none, "ExchangeCashDesk_MRC", "processSale")
        var params: List<String> = listOf()
        val param = Gson().toJson(this)
        params = params.plus(param)
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
}
