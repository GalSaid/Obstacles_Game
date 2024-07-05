package com.example.assignment1.Activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment1.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MenuActivity extends AppCompatActivity {

    private ExtendedFloatingActionButton main_BTN_arrows;
    private ExtendedFloatingActionButton main_BTN_sensors;
    private ExtendedFloatingActionButton main_BTN_scores;
    private final int SLOWMODE = 1000;
    private final int FASTMODE = 500;
    private boolean sensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();
        findViews();
        initViews();
    }

    private void initViews() {
        main_BTN_arrows.setOnClickListener((v) -> {
            sensor=false;
            openSpeedGameDialog();
        });
        main_BTN_sensors.setOnClickListener((v) -> {
            sensor=true;
            openSpeedGameDialog();
        });
        main_BTN_scores.setOnClickListener((v) -> moveToScores());
    }

    private  void openSpeedGameDialog() {
        new MaterialAlertDialogBuilder(this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog)
                .setTitle("Speed")
                .setMessage("Choose your starting speed")
                .setPositiveButton("Start slow", (dialog, which) -> moveToGame(sensor,SLOWMODE))
                .setNegativeButton("Start fast", (dialog, which) -> moveToGame(sensor,FASTMODE))
                .show();
    }

    private void moveToGame(boolean sensor, int delay){ //Move to gameActivity with the user's selections
        Intent i = new Intent(getApplicationContext(), GameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(getString(R.string.sensor_key_name), sensor);
        bundle.putInt(getString(R.string.speed_key_name),delay);
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }

    private void moveToScores(){
        Intent i = new Intent(getApplicationContext(), ScoresActivity.class);
        Bundle bundle = new Bundle();
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }

    private void findViews() {
        main_BTN_arrows = findViewById(R.id.main_BTN_arrows);
        main_BTN_scores = findViewById(R.id.main_BTN_scores);
        main_BTN_sensors = findViewById(R.id.main_BTN_sensors);

    }

    void getPermission() {  // A request for the user to approve location permission
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                finish();
                                // No location access granted.
                            }
                        }
                );
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }


}