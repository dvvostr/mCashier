package ru.studiq.mcashier.model.classes.adapters

import ProviderDataProductDetail
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.studiq.mcashier.R
import ru.studiq.mcashier.common.formatDouble
import ru.studiq.mcashier.model.classes.activities.common.CustomCompatActivity

class CartListAdapter(private val context: CustomCompatActivity, private val list: Array<ProviderDataProductDetail>) :
    RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            LayoutInflater.from(context).inflate(R.layout.activiry_cart_row, parent, false)
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.textArticle.text = list[position].info?.article ?: ""
        holder.textPLU.text = list[position].PLU
        holder.textDesc.text = list[position].info?.subTradeMarkName ?: ""
        holder.textName.text = list[position].caption
        holder.textColor.text = "${context.getString(R.string.cap_color)}: ${list[position].ColorID}"
        holder.textSize.text = "${context.getString(R.string.cap_size)}: ${list[position].SizeID}"
        holder.textQty.text = "1 ${context.getString(R.string.cap_pc)}."
        holder.textPrice.text = formatDouble(list[position].info?.price) ?: ""
    }

    override fun getItemCount() = list.size
}

class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val textArticle: TextView = view.findViewById(R.id.activity_cart_row_article)
    val textPLU: TextView = view.findViewById(R.id.activity_cart_row_PLU)
    val textDesc: TextView = view.findViewById(R.id.activity_cart_row_desc)
    val textName: TextView = view.findViewById(R.id.activity_cart_row_name)
    val textColor: TextView = view.findViewById(R.id.activity_cart_row_color)
    val textSize: TextView = view.findViewById(R.id.activity_cart_row_size)
    val textQty: TextView = view.findViewById(R.id.activity_cart_row_qty)
    val textPrice: TextView = view.findViewById(R.id.activity_cart_row_price)


}