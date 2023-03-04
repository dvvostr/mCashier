import com.google.gson.annotations.SerializedName
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
    @field:SerializedName("CarryOver") val CarryOver: String = ""
): java.io.Serializable {
}

