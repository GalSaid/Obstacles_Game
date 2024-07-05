package com.example.assignment1.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.assignment1.Fragments.Fragment_List;
import com.example.assignment1.Fragments.Fragment_Map;
import com.example.assignment1.Interfaces.ShowMapCallback;
import com.example.assignment1.R;
import com.google.android.material.button.MaterialButton;

public class ScoresActivity extends AppCompatActivity {

    private MaterialButton scores_BTN_return;
    private Fragment_Map fragmentMap;
    private Fragment_List fragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        findViews();
        scores_BTN_return.setOnClickListener(v-> moveToManu());
        fragmentMap = new Fragment_Map();

        fragmentList = new Fragment_List();
        fragmentList.setMapCallback(new ShowMapCallback() {
            @Override
            public void showLocationOnMap(double lat, double lng) {
                fragmentMap.setLocationOnMap(lat,lng);
            }
        });
        getSupportFragmentManager().beginTransaction()
                .add(R.id.scores_LAY_bottom, fragmentMap)
                .commit();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.scores_LAY_top, fragmentList)
                .commit();

    }

    private void moveToManu(){
        Intent i = new Intent(getApplicationContext(), MenuActivity.class);
        Bundle bundle = new Bundle();
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }


    private void findViews() {
        scores_BTN_return=findViewById(R.id.scores_BTN_return);
    }


}