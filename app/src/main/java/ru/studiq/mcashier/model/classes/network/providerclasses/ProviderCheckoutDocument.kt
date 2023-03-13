package ru.studiq.mcashier.model.classes.network.providerclasses

import android.content.Context
import com.google.gson.annotations.SerializedName

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
}