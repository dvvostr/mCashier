package ru.studiq.mcashier.UI.Customize

import android.content.Context
import android.preference.PreferenceCategory
import android.util.AttributeSet
import android.view.View

class CustomPreferenceCategory : PreferenceCategory {

    constructor(context: Context?) : super(context) {
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int ) : super(context, attrs, defStyle) {
    }

    override fun onBindView(view: View) {
        super.onBindView(view)
//        val titleView = view as? TextView
//        (titleView?.context as? Activity)?.setTheme(R.style.PreferenceScreenCategory);
//        if (titleView != null) {
//            titleView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorBackgroundWhite))
//            titleView.setTextColor(ContextCompat.getColor(view.context, R.color.colorTextDark))
//        }
//        titleView?.setTextColor(ContextCompat.getColor(view.context, R.color.colorTextDarkless))
    }
}