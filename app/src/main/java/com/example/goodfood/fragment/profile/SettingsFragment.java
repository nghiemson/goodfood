package com.example.goodfood.fragment.profile;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.goodfood.R;


public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

            addPreferencesFromResource(R.xml.pref_settings);

    }

}
