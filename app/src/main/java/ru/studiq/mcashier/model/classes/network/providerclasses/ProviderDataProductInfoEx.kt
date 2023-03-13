package ru.studiq.mcashier.model.classes.network.providerclasses

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import ru.studiq.mcashier.R
import ru.studiq.mcashier.common.formatDate
import ru.studiq.mcashier.interfaces.ICustomListActivityListener
import ru.studiq.mcashier.model.Settings
import ru.studiq.mcashier.model.classes.network.CreateProviderRequest
import ru.studiq.mcashier.model.classes.network.ProviderDataBody
import ru.studiq.mcashier.model.classes.network.ProviderRequestMethodStatic
import ru.studiq.mcashier.model.classes.network.ProviderRequestSystemType
import java.io.Serializable
import java.util.*

interface IDataProductInfoExActivityListener {
    fun onSuccess(sender: Context?, code: Int, msg: String, data: ProviderDataProductInfoEx?) {}
    fun onEmpty(sender: Context?) {}
    fun onError(sender: Context?, code: Int, msg: String) {}
}

data class ProviderDataProductInfoExItems (
    @field:SerializedName("items") var items: MutableList<ProviderDataProductInfoEx> = arrayListOf()
): CustomProviderData() {
    companion object {
    }
    public var total: Double = 0.0
        get(){
            var out: Double = 0.0
            items.forEach { item ->
                out = out.plus(item?.currentPrice ?: 0.0)
            }
            return out
        }
    public fun calculate() {
        var i: Int = 1
        items.forEach({
            it.posIndex = i
            i = i.plus(1)
        })
    }
    public fun check(value: ProviderDataProductInfoEx): Boolean {
        val stockQty = value?.stockQuantity ?: 0.0
        val saleQty = (items.filter { it.barcode == value.barcode }.map{ it.qty }.sum() + value.qty)
        return (stockQty > 0.0 && stockQty >= saleQty)
    }
    public val asCheckoutDocument: ProviderCheckoutDocument
    get(){
        calculate()
        val header: ProviderCheckoutDocumentHeader = ProviderCheckoutDocumentHeader(
            "",
            formatDate(Date(), "yyyy-MM-dd"),
            Settings.Application.currentDepartment?.id ?: "",
            Settings.Application.cashbox,
            "",
            Settings.Application.currentUser?.staffCode ?: ""
        )
        var articles: List<ProviderCheckoutDocumentArticle> = listOf()
        this.items.forEach({
            articles = articles.plus(it.asCheckoutDocumentArticle)
        })
        var staffs: List<ProviderCheckoutDocumentStaff> = listOf()
        this.items.forEach({
            staffs = staffs.plus(
                ProviderCheckoutDocumentStaff(
                    it.posIndex,
                    0,
                    Settings.Application.currentUser?.hrID ?: -1,
                    Settings.Application.currentUser?.staffCode ?: "",
                    Settings.Application.currentUser?.userName ?: "",
                    (it.currentPrice * it.qty),
                    100
                )
            )
        })
        var payment: List<ProviderCheckoutDocumentPayment> = listOf()
        payment = payment.plus(ProviderCheckoutDocumentPayment("БКарта", total))
        var peservesList: List<ProviderCheckoutDocumentReservesList> = listOf()

        return ProviderCheckoutDocument(
            1,
            header,
            articles,
            staffs,
            payment,
            peservesList
        )
    }
}
data class ProviderDataProductInfoEx(
    @field:SerializedName("CurrentPrice") val currentPrice: Double,
    @field:SerializedName("OriginalPrice") val originalPrice: Double,
    @field:SerializedName("VAT") val VAT: Double,
    @field:SerializedName("AvailableQuantity") val stockQuantity: Double,
    @field:SerializedName("UniqueMarking") val uniqueMarking: Int,
    @field:SerializedName("Articul") val article: String,
    @field:SerializedName("ItemID") val PLU: String,
    @field:SerializedName("ColorID") val color: String,
    @field:SerializedName("SizeID") val size: String,
    @field:SerializedName("CSCombination") val colorSize: String,
    @field:SerializedName("ItemDescription") val description: String,
    @field:SerializedName("Category") val category: String,
    @field:SerializedName("TradeMarkID") val trademark: String,
    @field:SerializedName("TradeMark") val trademarkName: String,
    @field:SerializedName("SeasonID") val season: String,
    @field:SerializedName("PrincipalName") val principalName: String,
    @field:SerializedName("PrincipalPhone") val principalPhone: String,
    @field:SerializedName("PrincipalINN") val principalINN: String
): CustomProviderData() {
    public var barcode: String = ""
    public var qty: Double = 1.0
    public var posIndex: Int = -1
    companion object {
    }
    public val asCheckoutDocumentArticle: ProviderCheckoutDocumentArticle
    get(){
        return ProviderCheckoutDocumentArticle(
            this.barcode,
            this.posIndex,
            this.qty,
            this.currentPrice,
            this.currentPrice,
            this.originalPrice,
            this.VAT,
            this.category
        )
    }
}

fun ProviderDataProductInfoEx.Companion.load(sender: Context?, params: String, listener: IDataProductInfoExActivityListener) {
    // TODO проверить обработку ошибок при ошибке аксапта
    val request = CreateProviderRequest(UUID.randomUUID().toString(), ProviderRequestSystemType.axapta, ProviderRequestMethodStatic.none, "InventOnLineKassa_OnHand", "getinventOnHandToLocationIdExt")
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
            val json = Gson().toJson((data as? ProviderDataBody)?.data)
            val data: ProviderDataProductInfoEx? = Gson().fromJson(json, object : TypeToken<ProviderDataProductInfoEx?>() {}.type)
            data?.barcode = params
            data?.qty = 1.0
            listener.onSuccess(sender, code, msg, data)
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