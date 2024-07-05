package com.example.assignment1.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.assignment1.Interfaces.MoveCallback;
import com.example.assignment1.Interfaces.PlayerCallback;
import com.example.assignment1.Logic.GameManager;
import com.example.assignment1.Model.Player;
import com.example.assignment1.Utilities.MSP;
import android.Manifest;


import com.example.assignment1.Utilities.MoveDetector;
import com.example.assignment1.Utilities.MyBackgroundMusic;
import com.example.assignment1.Utilities.MySignal;
import com.example.assignment1.R;
import com.example.assignment1.Utilities.Type;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Collections;

public class GameActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final Handler handler = new Handler();
    private final int NUM_OF_ROUTES = 5;
    private final int NUM_OF_LINES = 6; //included the player
    private final int INITIAL_LIVES = 3;
    private int delay;
    private boolean sensorMode;
    private AppCompatImageView[][] game_IMG_matrix;
    private AppCompatImageView[] game_IMG_hearts;
    private MaterialButton game_BTN_right;
    private MaterialButton game_BTN_left;
    private MaterialButton game_BTNCARD_ok;
    private RelativeLayout game_LAY_arrowsLayout;
    private MaterialTextView game_LBL_score;
    private CardView game_CARDVIEW_gameOver;
    private MaterialTextView game_LBLCARD_final_score;
    private EditText game_EDITTEXT_name;
    private GameManager gameManager;
    private MoveDetector moveDetector;
    private boolean addItem; //add spray/garbage every 2 ticks
    private FusedLocationProviderClient mFusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        findViews();
        initViews();
        initMoveDetector();
        addItem = true;
        gameManager = new GameManager(NUM_OF_ROUTES, NUM_OF_LINES, INITIAL_LIVES);
        updateLivesUI();
        updateCockroachUI(NUM_OF_ROUTES / 2); // initial location
        updateSprayAndGarbageUI();
        game_BTN_left.setOnClickListener(v -> movePlayerLeft());
        game_BTN_right.setOnClickListener(v -> movePlayerRight());
        game_BTNCARD_ok.setOnClickListener(v->  savePlayerInfoAndMoveToScoreTable());
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        MyBackgroundMusic.getInstance().playMusic();
        start();
        if(sensorMode)
            moveDetector.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MyBackgroundMusic.getInstance().pauseMusic();
        stop();
        if(sensorMode)
            moveDetector.stop();
    }

    @SuppressLint("MissingPermission")
    private void savePlayerInfoAndMoveToScoreTable() {
        if(game_EDITTEXT_name.length()==0)
            MySignal.getInstance().toast("Please enter your name");
        else
        {
            Player newPlayer= new Player(game_EDITTEXT_name.getText().toString(),gameManager.getScore());
            mFusedLocationClient.getLastLocation()  //Get the user location
                    .addOnSuccessListener(location -> {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            newPlayer.setLat(location.getLatitude());
                            newPlayer.setLng(location.getLongitude());
                        }
                        ArrayList<Player> players= MSP.getInstance().readPlayersList(getString(R.string.players_list));
                        players.add(newPlayer);
                        MSP.getInstance().savePlayersList(getString(R.string.players_list),players);
                        moveToScoreTable();
                    })
                    .addOnFailureListener(e -> {
                        Log.d("MapDemoActivity", "Error trying to get last GPS location");
                    });
        }
    }

    private void tick() {
        nextStep();
    }

    private final Runnable runnable = new Runnable() {
        public void run() {
            handler.postDelayed(runnable, delay);
            tick();

        }
    };

    private void start() {
        handler.postDelayed(runnable, delay);
    }
    private void stop() { handler.removeCallbacks(runnable); }

    private void movePlayerLeft(){
        if(game_IMG_matrix[NUM_OF_LINES - 1][0].getVisibility() == View.VISIBLE)
            return;
        gameManager.setCockroachCol(-1);
        updateCockroachUI(gameManager.getCockroachCol());
    }
    private void movePlayerRight(){
        if(game_IMG_matrix[NUM_OF_LINES - 1][NUM_OF_ROUTES - 1].getVisibility() ==  View.VISIBLE)
            return;
        gameManager.setCockroachCol(1);
        updateCockroachUI(gameManager.getCockroachCol());
    }

    private void nextStep() {
        if (gameManager.negativeCollisionExists()) {
            negativeCollision();
            if (gameManager.getCurrLives() == 0) {
                gameOver();
                return;
            }
        }
        else if(gameManager.positiveCollisionExists())
            positiveCollision();
        gameManager.updateMatrix();
        if (addItem) {
            gameManager.addItem();
        }
        addItem = !addItem;
        updateSprayAndGarbageUI();
    }


    private void positiveCollision() {  //Collision with the garbage
        game_LBL_score.setText(String.valueOf(gameManager.getScore()));
        MySignal.getInstance().vibrate();
        MySignal.getInstance().playSound(R.raw.sound_coins);
    }


    private void negativeCollision() { //Collision with the spray
        gameManager.decreaseLive();
        updateLivesUI();
        MySignal.getInstance().vibrate();
        MySignal.getInstance().playSound(R.raw.sound_spray);
    }

    private void gameOver() {
        stop();
        game_LBLCARD_final_score.setText(String.valueOf(gameManager.getScore()));
        game_CARDVIEW_gameOver.setVisibility(View.VISIBLE);
        if(sensorMode)
            moveDetector.stop();
        else
            game_LAY_arrowsLayout.setVisibility(View.INVISIBLE);
    }

    private void moveToScoreTable(){
        Intent i = new Intent(getApplicationContext(), ScoresActivity.class);
        Bundle bundle = new Bundle();
        i.putExtras(bundle);
        startActivity(i);
        finish();
    }

    private void updateLivesUI() {
        int SZ = game_IMG_hearts.length;

        for (int i = 0; i < SZ; i++) {
            game_IMG_hearts[i].setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < SZ - gameManager.getCurrLives(); i++) {
            game_IMG_hearts[i].setVisibility(View.INVISIBLE);
        }
    }

    private void updateSprayAndGarbageUI() {
        Type[][] matrix = gameManager.getMatrix();
        for (int i = 0; i < matrix.length - 1; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == Type.GARBAGE)
                    game_IMG_matrix[i][j].setImageResource(R.drawable.img_garbage);
                else if (matrix[i][j] == Type.SPRAY)
                    game_IMG_matrix[i][j].setImageResource(R.drawable.img_spray);
               else
                    game_IMG_matrix[i][j].setImageResource(0);
            }
        }
    }

    private void updateCockroachUI(int location) {
        for (int i = 0; i < NUM_OF_ROUTES; i++) {
            if (i == location)
                game_IMG_matrix[NUM_OF_LINES - 1][i].setVisibility(View.VISIBLE);
            else
                game_IMG_matrix[NUM_OF_LINES - 1][i].setVisibility(View.INVISIBLE);
        }
    }

    private void initMoveDetector() { //Sensors
        moveDetector = new MoveDetector(this,
                new MoveCallback() {
                    @Override
                    public void moveLeft() {
                        movePlayerLeft();
                    }

                    @Override
                    public void moveRight() {
                        movePlayerRight();
                    }

                    @Override
                    public void moveUp() { //Slower
                        delay=1000;
                    }

                    @Override
                    public void moveDown() { //Faster
                        delay=500;
                    }
                });
    }

    private void initViews() {
        game_CARDVIEW_gameOver.setVisibility(View.INVISIBLE);
        Intent prev = getIntent();
        delay= prev.getExtras().getInt(getString(R.string.speed_key_name));
        sensorMode=prev.getExtras().getBoolean(getString(R.string.sensor_key_name));
        if(sensorMode)
            game_LAY_arrowsLayout.setVisibility(View.GONE);
    }

    private void findViews() {

        game_IMG_hearts = new AppCompatImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3),
        };
        game_IMG_matrix = new AppCompatImageView[][]{
                {findViewById(R.id.game_IMG_spray00),
                        findViewById(R.id.game_IMG_spray01),
                        findViewById(R.id.game_IMG_spray02),
                        findViewById(R.id.game_IMG_spray03),
                        findViewById(R.id.game_IMG_spray04)},
                {findViewById(R.id.game_IMG_spray10),
                        findViewById(R.id.game_IMG_spray11),
                        findViewById(R.id.game_IMG_spray12),
                        findViewById(R.id.game_IMG_spray13),
                        findViewById(R.id.game_IMG_spray14)},
                {findViewById(R.id.game_IMG_spray20),
                        findViewById(R.id.game_IMG_spray21),
                        findViewById(R.id.game_IMG_spray22),
                        findViewById(R.id.game_IMG_spray23),
                        findViewById(R.id.game_IMG_spray24)},
                {findViewById(R.id.game_IMG_spray30),
                        findViewById(R.id.game_IMG_spray31),
                        findViewById(R.id.game_IMG_spray32),
                        findViewById(R.id.game_IMG_spray33),
                        findViewById(R.id.game_IMG_spray34)},
                {findViewById(R.id.game_IMG_spray40),
                        findViewById(R.id.game_IMG_spray41),
                        findViewById(R.id.game_IMG_spray42),
                        findViewById(R.id.game_IMG_spray43),
                        findViewById(R.id.game_IMG_spray44)},
                {findViewById(R.id.game_IMG_cockroach0),
                        findViewById(R.id.game_IMG_cockroach1),
                        findViewById(R.id.game_IMG_cockroach2),
                        findViewById(R.id.game_IMG_cockroach3),
                        findViewById(R.id.game_IMG_cockroach4)}
        };
        game_BTN_right = findViewById(R.id.game_BTN_right);
        game_BTN_left = findViewById(R.id.game_BTN_left);
        game_LAY_arrowsLayout = findViewById(R.id.game_LAY_arrowsLayout);
        game_LBL_score = findViewById(R.id.game_LBL_score);
        game_BTNCARD_ok = findViewById(R.id.game_BTNCARD_ok);
        game_CARDVIEW_gameOver = findViewById(R.id.game_CARDVIEW_gameOver);
        game_LBLCARD_final_score = findViewById(R.id.game_LBLCARD_final_score);
        game_EDITTEXT_name = findViewById(R.id.game_EDITTEXT_name);
    }

}