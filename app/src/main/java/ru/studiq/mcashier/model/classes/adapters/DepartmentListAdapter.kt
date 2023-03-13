package ru.studiq.mcashier.model.classes.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.studiq.mcashier.R
import ru.studiq.mcashier.interfaces.IObjectClickListener
import ru.studiq.mcashier.model.classes.network.providerclasses.ProviderDataDepartment

class DepartmentListAdapter( private val list: List<ProviderDataDepartment> ) : RecyclerView.Adapter<DepartmentListAdapter.ViewHolder>() {
    private lateinit var onItemClickListener: AdapterView.OnItemClickListener

    lateinit var objectClickEvent: IObjectClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.activity_department_row_item, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.item = list[position]
        holder.codeView.text = list[position].id
        holder.captionView.text = list[position].caption
        holder.descriptionView.text = list[position].description
        holder.setEvent(object: IObjectClickListener {
            override fun onObjectItemClick(view: View, data: Any?) {
                objectClickEvent?.onObjectItemClick(view, data)
            }
        })
    }
    override fun getItemCount(): Int = list.size

    //************************************************//
    class ViewHolder(itemsView: View): RecyclerView.ViewHolder(itemsView), View.OnClickListener {
        internal var item: ProviderDataDepartment? = null
        internal var codeView: TextView
        internal var captionView: TextView
        internal var descriptionView: TextView
        internal lateinit var iObjectClickListener: IObjectClickListener

        init {
            this.codeView = itemsView.findViewById(R.id.departmentlist_row_code)
            this.captionView = itemsView.findViewById(R.id.departmentlist_row_caption)
            this.descriptionView = itemsView.findViewById(R.id.departmentlist_row_description)
            this.itemView.setOnClickListener(this)
        }
        fun setEvent(listener: IObjectClickListener) {
            this.iObjectClickListener = listener
        }
        override fun onClick(view: View?) {
            if (view != null ) {
                this.iObjectClickListener?.onObjectItemClick(view!!, item)
            }
        }
    }
}

