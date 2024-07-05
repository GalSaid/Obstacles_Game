package com.example.assignment1;

import android.app.Application;

import com.example.assignment1.Utilities.MSP;
import com.example.assignment1.Utilities.MyBackgroundMusic;
import com.example.assignment1.Utilities.MySignal;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MySignal.init(this);
        MSP.init(this);
        MyBackgroundMusic.init(this);
        MyBackgroundMusic.getInstance().setResourceId(R.raw.sound_background);
    }
}
