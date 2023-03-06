package ru.studiq.mcashier.UI.Fragments
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.studiq.mcashier.R

class SalesActionFragment : BottomSheetDialogFragment(), View.OnClickListener {
    private var mListener: SalesItemClickListener? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,): View? {
        return inflater.inflate(R.layout.custom_sale_actions, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.sale_action_card_01).setOnClickListener(this)
        view.findViewById<View>(R.id.sale_action_card_02).setOnClickListener(this)
        view.findViewById<View>(R.id.sale_action_card_03).setOnClickListener(this)
        view.findViewById<View>(R.id.sale_action_card_04).setOnClickListener(this)
        view.findViewById<View>(R.id.sale_action_card_05).setOnClickListener(this)
        view.findViewById<View>(R.id.sale_action_card_06).setOnClickListener(this)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is SalesItemClickListener) {
            context
        } else {
            throw RuntimeException(context.toString() + " must implement ItemClickListener" )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onClick(view: View) {
        val tvSelected = view as TextView
        mListener!!.onItemClick(tvSelected.text.toString())
        dismiss()
    }

    interface SalesItemClickListener {
        fun onItemClick(item: String?)
    }

    companion object {
        const val TAG = "SalesActionDialog"
        fun newInstance(): SalesActionFragment {
            return SalesActionFragment()
        }
    }
}