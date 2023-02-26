package ru.studiq.mcashier.UI.Classes

import android.content.Context
import ru.studiq.mcashier.interfaces.ICustomListActivityListener
import ru.studiq.mcashier.model.classes.activities.common.CustomCompatActivity

open class CustomListActivity: CustomCompatActivity() {
    companion object {
        open fun load(sender: Context?, listener: ICustomListActivityListener) {
            listener.onSuccess(sender, 1, "Text", null)
        }
    }
    //********************************************//
    override fun setupActivity() {
        super.setupActivity()
        this.supportActionBar?.elevation = 0f
    }

}