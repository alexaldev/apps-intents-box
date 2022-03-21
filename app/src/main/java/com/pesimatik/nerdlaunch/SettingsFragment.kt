package com.pesimatik.nerdlaunch

import android.os.Bundle
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.pesimatik.app_box.R

class SettingsFragment : PreferenceFragmentCompat() {

    interface Callbacks {
        fun onFontSizeReselected()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_settings, rootKey)
        val fontSizesPreference : ListPreference? = findPreference("text_size")
        fontSizesPreference?.setOnPreferenceChangeListener { _, _ ->
            (activity as Callbacks).onFontSizeReselected()
            true
        }
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}