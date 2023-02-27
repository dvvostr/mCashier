package ru.studiq.mcashier.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.studiq.mcashier.R
import ru.studiq.mcashier.interfaces.ICardItemClickListener
import ru.studiq.mcashier.model.classes.items.MainCardItem
import java.security.AccessController.getContext

class MainCardAdapter( private val list: List<MainCardItem> ) : RecyclerView.Adapter<MainCardAdapter.ViewHolder>() {
    private lateinit var onItemClickListener: AdapterView.OnItemClickListener

    lateinit var cardItemClickEvent: ICardItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.activity_main_card_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cardView.setCardBackgroundColor(ContextCompat.getColor(holder.cardView.context, R.color.background_semi_light))
        holder.imageView.setImageResource(list[position].image)
        holder.captionView.text = list[position].caption
        holder.setEvent(object: ICardItemClickListener {
            override fun onCardItemClick (view: View, position: Int) {
                cardItemClickEvent?.onCardItemClick(view, position)
            }
        })
    }
    override fun getItemCount(): Int = list.size

    //************************************************//
    class ViewHolder(itemsView: View): RecyclerView.ViewHolder(itemsView), View.OnClickListener {
        internal var imageView: ImageView
        internal var captionView: TextView
        internal var cardView: CardView
        internal lateinit var iCardItemClickListener: ICardItemClickListener

        init {
            this.cardView = itemsView.findViewById(R.id.main_card_cardview) as CardView
            this.imageView = itemsView.findViewById(R.id.main_card_imageView) as ImageView
            this.captionView = itemsView.findViewById(R.id.main_card_textView)
            this.itemView.setOnClickListener(this)
        }
        fun setEvent(iCardItemClickListener: ICardItemClickListener) {
            this.iCardItemClickListener = iCardItemClickListener
        }
        override fun onClick(view: View?) {
            if (view != null ) {
                this.iCardItemClickListener?.onCardItemClick(view!!, adapterPosition)
            }
        }
    }
}

