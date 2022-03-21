package com.pesimatik.appbox

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.pesimatik.app_box.R

class AppBoxActivity : AppCompatActivity(), SettingsFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, MainFragment.newInstance())
            .commit()
    }

    override fun attachBaseContext(newBase: Context?) {

        val scale = FontSizeManager.getFontScale(PreferenceManager.getDefaultSharedPreferences(newBase!!))
        val newConfig = Configuration(newBase.resources.configuration).apply {
            fontScale = scale
        }
        applyOverrideConfiguration(newConfig)
        super.attachBaseContext(newBase)
    }

    override fun onFontSizeReselected() {
        AppCompatActivity@this.recreate()
    }
}
