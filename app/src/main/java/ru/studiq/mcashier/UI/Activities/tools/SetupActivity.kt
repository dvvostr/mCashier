package ru.studiq.mcashier.UI.Activities.tools

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.preference.*
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import ru.studiq.mcashier.R
import ru.studiq.mcashier.model.classes.activities.common.CustomCompatActivity
import ru.studiq.mcashier.common.Common


class SetupActivity : CustomCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun setupActivity() {
        super.setupActivity()
        setContentView(R.layout.activity_setup)
        this.caption = getString(R.string.cap_settings).toUpperCase()
        this.setIcon(R.drawable.icon_title_mixer_spacer)
        if (fragmentManager.findFragmentById(android.R.id.content) == null) {
            fragmentManager.beginTransaction()
                .add(android.R.id.content, SettingsFragment()).commit()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onMenuItemExecute(item: MenuItem) {
        super.onMenuItemExecute(item)
        when (item.itemId){
            R.id.menu_settings_about -> {
                this.handleShowAbout()
            }
            else -> return
        }
    }

    private fun handleShowAbout(){
        try {
            val pInfo: PackageInfo =
                this.getPackageManager().getPackageInfo(this.getPackageName(), 0)
            val version = pInfo.versionName
            val packageName = pInfo.packageName
            Common.AlertDialog.show(
                this,
                getString(R.string.app_name),
                "${packageName}\nver. ${version}",
                true
            )
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } finally {
        }
    }
    companion object {
        /**
         * A preference value change listener that updates the preference's summary
         * to reflect its new value.
         */
        private val sBindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
            val stringValue = value.toString()
            if (preference is ListPreference) {
                // For list preferences, look up the correct display value in the preference's 'entries' list.
                val listPreference = preference
                val index = listPreference.findIndexOfValue(stringValue)
                // Set the summary to reflect the new value.
                preference.setSummary(
                    if (index >= 0)
                        listPreference.entries[index]
                    else
                        null
                )
            } else if (preference is RingtonePreference) {
                // For ringtone preferences, look up the correct display value using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) { // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary("Silent")
                } else {
                    val ringtone = RingtoneManager.getRingtone(
                        preference.getContext(), Uri.parse(stringValue)
                    )
                    if (ringtone == null) { // Clear the summary if there was a lookup error.
                        preference.setSummary(null)
                    } else { // Set the summary to reflect the new ringtone display name.
                        val name = ringtone.getTitle(preference.getContext())
                        preference.setSummary(name)
                    }
                }
            } else { // For all other preferences, set the summary to the value's simple string representation.
                preference.summary = stringValue
            }
            true
        }

        private fun bindPreferenceSummaryToValue(preference: Preference) {
            // Set the listener to watch for value changes.
            preference.onPreferenceChangeListener = sBindPreferenceSummaryToValueListener
            // Trigger the listener immediately with the preference's current value.
            sBindPreferenceSummaryToValueListener.onPreferenceChange(
                preference, PreferenceManager.getDefaultSharedPreferences(
                    preference.context
                ).getString(preference.key, "")
            )
        }
    }
    class SettingsFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            this.addPreferencesFromResource(R.xml.preferences)
            bindPreferenceSummaryToValue(findPreference(getString(R.string.setting_id_connection)))
            bindPreferenceSummaryToValue(findPreference(getString(R.string.setting_hw_devicetype)))
        }
    }
}