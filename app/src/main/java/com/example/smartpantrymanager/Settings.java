package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//this is just a simple screen for the preferences which allows the
//user to choose measurements//

public class Settings extends AppCompatActivity {

    public static final String PREFS_NAME = "smart_pantry_prefs";
    public static final String KEY_USE_METRIC = "use_metric";
    public static final String KEY_EXPIRY_ALERTS = "expiry_alerts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        Switch unitsSwitch = findViewById(R.id.switchUnits);
        TextView unitsExplanation = findViewById(R.id.textUnitsExplanation);
        Switch expirySwitch = findViewById(R.id.switchExpiryAlerts);

        boolean useMetric = prefs.getBoolean(KEY_USE_METRIC, true);
        boolean expiryAlerts = prefs.getBoolean(KEY_EXPIRY_ALERTS, true);

        unitsSwitch.setChecked(!useMetric);
        updateUnitsLabel(unitsExplanation, useMetric);
        expirySwitch.setChecked(expiryAlerts);

        unitsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            boolean nowUsingMetric = !isChecked;
            updateUnitsLabel(unitsExplanation, nowUsingMetric);
            prefs.edit().putBoolean(KEY_USE_METRIC, nowUsingMetric).apply();
        });

        expirySwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean(KEY_EXPIRY_ALERTS, isChecked).apply());
    }

    private void updateUnitsLabel(TextView label, boolean useMetric) {
        label.setText(useMetric ? "Metric (grams, ml)" : "Imperial (oz, cups)");
    }
}