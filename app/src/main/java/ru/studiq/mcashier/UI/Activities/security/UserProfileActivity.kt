package ru.studiq.mcashier.UI.Activities.security

import android.os.Bundle
import ru.studiq.mcashier.R
import ru.studiq.mcashier.model.Settings
import ru.studiq.mcashier.model.classes.activities.common.CustomCompatActivity

class UserProfileActivity : CustomCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
    }

    override fun setupActivity() {
        super.setupActivity()
        supportActionBar?.title = Settings.Application.currentUser?.userName ?: ""
        supportActionBar?.subtitle = Settings.Application.currentUser?.department ?: ""
    }
}