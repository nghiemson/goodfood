package com.example.goodfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_settings);


        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        switch (id) {
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                getApplicationContext().deleteDatabase("recipe");
                Intent newIncome = new Intent(this, LoginActivity.class);
                this.startActivity(newIncome);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.noti_1_key))) {
            sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.noti_1_default));
        }

            if(key.equals(getString(R.string.noti_2_key))){
                sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.noti_2_default));
            }
            if(key.equals(getString(R.string.noti_3_key))){
                sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.noti_3_default));
            }
            if(key.equals(getString(R.string.privacy_1_key))){
                sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.privacy_1_default));
            }
            if(key.equals(getString(R.string.privacy_2_key))){
                sharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.privacy_2_default));
            }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}