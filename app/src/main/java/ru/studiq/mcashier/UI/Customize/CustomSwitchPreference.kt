package ru.studiq.mcashier.UI.Customize

import android.content.Context
import android.preference.SwitchPreference
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import ru.studiq.mcashier.R

class CustomSwitchPreference: SwitchPreference {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    override fun onCreateView(parent: ViewGroup?): View? {
        return super.onCreateView(parent)
    }

    override fun onBindView(view: View?) {
        super.onBindView(view)
        if (view != null) {
            view?.setBackgroundColor(ContextCompat.getColor(view.context, R.color.background))
        }
    }
}