import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import ru.studiq.mcashier.R
import ru.studiq.mcashier.UI.Activities.security.UserListActivity
import ru.studiq.mcashier.interfaces.ICustomListActivityListener
import ru.studiq.mcashier.interfaces.IProviderClientListener
import ru.studiq.mcashier.model.Settings
import ru.studiq.mcashier.model.classes.network.*
import ru.studiq.mcashier.model.classes.network.providerclasses.CustomProviderData
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataProductInfo
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataUser
import java.io.Serializable

interface IDataProductDetailActivityListener {
    fun onSuccess(sender: Context?, code: Int, msg: String, data: ProviderDataProductDetail?) {}
    fun onEmpty(sender: Context?) {}
    fun onError(sender: Context?, code: Int, msg: String) {}
}
data class ProviderDataProductDetailItems (
    @field:SerializedName("items") var items: Array<ProviderDataProductDetail> = arrayOf()
): CustomProviderData() {
    companion object {
    }
    public var total: Double = 0.0
        get(){
        var out: Double = 0.0
        items.forEach { item ->
            out = out.plus(item.info?.price ?: 0.0)
        }
        return out
    }
}
data class ProviderDataProductDetail (
    @field:SerializedName("BarCode") val barcode: String,
    @field:SerializedName("PLU") val PLU: String,
    @field:SerializedName("CSCombinationID") val combinationCode: String,
    @field:SerializedName("CSCombination") val combinationName: String = "",
    @field:SerializedName("ItemDescription") val caption: String = "",
    @field:SerializedName("SaleTypeID") val salesType: Int = -1,
    @field:SerializedName("SaleTypeDescription") val salesTypeName: String = "",
    @field:SerializedName("MedService") val medService: Int = -1,
    @field:SerializedName("NomGroupID") val nomGroup: Int = -1,
    @field:SerializedName("NomGroupName") val nomGroupName: String = "",
    @field:SerializedName("ExtSystemCode") val extSystemCode: String = "",
    @field:SerializedName("IsService") val isService: Int,
    @field:SerializedName("TMId") val tm: String = "",
    @field:SerializedName("TSUMSubTMID") val TSUMSubTMID: String = "",
    @field:SerializedName("TSUMKTTID") val TSUMKTTID: String = "",
    @field:SerializedName("ActionTag") val ActionTag: String = "",
    @field:SerializedName("UniqueMarking") val UniqueMarking: String = "",
    @field:SerializedName("SeasonID") val SeasonID: String = "",
    @field:SerializedName("ColorID") val ColorID: String = "",
    @field:SerializedName("SizeID") val SizeID: String = "",
    @field:SerializedName("CarryOver") val CarryOver: String = "",
    @field:SerializedName("Info") var info: ProviderDataProductInfo? = null
): CustomProviderData() {
    companion object {
    }
}

fun ProviderDataProductDetail.Companion.load(sender: Context?, params: String, listener: IDataProductDetailActivityListener) {
    val request = CreateProviderRequest(java.util.UUID.randomUUID().toString(), ProviderRequestSystemType.cashrigester, ProviderRequestMethodStatic.none, "", "GetBarCodeInfo")
    CustomProviderData.Companion.load(sender, request, params, object : ICustomListActivityListener {
        override fun onSuccess(sender: Context?, code: Int, msg: String, data: Serializable?) {
            super.onSuccess(sender, code, msg, data)
            val json = Gson().toJson((data as? ProviderDataBody)?.data)
            val list: List<ProviderDataProductDetail?> = Gson().fromJson(Gson().toJson((data as? ProviderDataBody)?.data), object : TypeToken<List<ProviderDataProductDetail?>?>() {}.type)
            listener.onSuccess(sender, code, msg, list.first())
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

