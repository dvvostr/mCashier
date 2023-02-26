package ru.studiq.mcashier.UI.Customize

import android.content.Context
import android.preference.EditTextPreference
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import ru.studiq.mcashier.R


class CustomEditTextPreference : EditTextPreference {
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    override fun onCreateView(parent: ViewGroup?): View? {
        this.summary = this.text
        return super.onCreateView(parent)
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        super.onDialogClosed(positiveResult)
        if (positiveResult) {
            this.summary = text
        }
    }
    override fun onBindView(view: View?) {
        super.onBindView(view)
        if (view != null) {
            view?.setBackgroundColor(ContextCompat.getColor(view.context, R.color.background))
        }
    }
    override fun callChangeListener(newValue: Any?): Boolean {
        return super.callChangeListener(newValue)
    }
}
