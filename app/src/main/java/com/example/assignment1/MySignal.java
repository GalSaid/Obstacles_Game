package com.example.assignment1;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MySignal {

    private static MySignal instance;
    private Context context;

    private MySignal(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new MySignal(context.getApplicationContext());
        }
    }

    public static MySignal getInstance() {
        return instance;
    }

    public void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void vibrate() {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

    public void playSound() {
        try {
            //Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.sound_spray);
            Ringtone r = RingtoneManager.getRingtone(context, soundUri);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
