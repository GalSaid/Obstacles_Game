package com.example.assignment1;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    private final int NUM_OF_ROUTES = 3;
    private final int NUM_OF_LINES = 6; //included the player
    private final int INITIAL_LIVES = 3;
    private final int DELAY = 1000;
    private AppCompatImageView[][] game_IMG_matrix;
    private AppCompatImageView[] game_IMG_hearts;
    private MaterialButton game_BTN_right;
    private MaterialButton game_BTN_left;
    private GameManager gameManager;
    private boolean addSpray; //add spray every 2 ticks


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        addSpray = true;
        gameManager = new GameManager(NUM_OF_ROUTES, NUM_OF_LINES, INITIAL_LIVES);
        updateLivesUI();
        updateCockroachUI(NUM_OF_ROUTES / 2); // initial location
        updateSprayUI();
        game_BTN_left.setOnClickListener(v -> moveCockroach(true));
        game_BTN_right.setOnClickListener(v -> moveCockroach(false));
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }

    private void tick() {
        nextStep();
    }

    private final Runnable runnable = new Runnable() {
        public void run() {
            handler.postDelayed(runnable, DELAY);
            tick();

        }
    };

    private void start() {
        handler.postDelayed(runnable, DELAY);
    }
    private void stop() { handler.removeCallbacks(runnable); }

    private void moveCockroach(boolean isLeft) { //move the player according to the arrows
        int newPosition;
        if (isLeft && game_IMG_matrix[NUM_OF_LINES - 1][0].getVisibility() == View.VISIBLE)
            return;
        if (!isLeft && game_IMG_matrix[NUM_OF_LINES - 1][NUM_OF_ROUTES - 1].getVisibility() == View.VISIBLE)
            return;
        newPosition = gameManager.setCockroachCol(isLeft ? -1 : 1);
        updateCockroachUI(newPosition);
    }

    private void nextStep() {
        if (gameManager.collisionExists()) {
            collision();
            if (gameManager.getCurrLives() == 0) {
                lose();
                return;
            }
        }
        gameManager.updateMatrix();
        if (addSpray) {
            gameManager.addSpray();
        }
        addSpray = !addSpray;
        updateSprayUI();
    }

    private void collision() {
        gameManager.decreaseLive();
        updateLivesUI();
        MySignal.getInstance().vibrate();
        MySignal.getInstance().playSound();
    }

    private void lose() {
        MySignal.getInstance().toast("You lose");
        stop();
        gameManager.resetGame(); //endless game
        updateLivesUI();
        updateCockroachUI(NUM_OF_ROUTES / 2);
        updateSprayUI();
        start();
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

    private void updateSprayUI() {
        Type[][] matrix = gameManager.getMatrix();
        for (int i = 0; i < matrix.length - 1; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == Type.EMPTY)
                    game_IMG_matrix[i][j].setImageResource(0);
                else if (matrix[i][j] == Type.SPRAY)
                    game_IMG_matrix[i][j].setImageResource(R.drawable.img_spray);
                // else it will be in the next part of the game
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

    private void findViews() {

        game_IMG_hearts = new AppCompatImageView[]{
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3),
        };
        game_IMG_matrix = new AppCompatImageView[][]{
                {findViewById(R.id.game_IMG_spray00),
                        findViewById(R.id.game_IMG_spray01),
                        findViewById(R.id.game_IMG_spray02)},
                {findViewById(R.id.game_IMG_spray10),
                        findViewById(R.id.game_IMG_spray11),
                        findViewById(R.id.game_IMG_spray12)},
                {findViewById(R.id.game_IMG_spray20),
                        findViewById(R.id.game_IMG_spray21),
                        findViewById(R.id.game_IMG_spray22)},
                {findViewById(R.id.game_IMG_spray30),
                        findViewById(R.id.game_IMG_spray31),
                        findViewById(R.id.game_IMG_spray32)},
                {findViewById(R.id.game_IMG_spray40),
                        findViewById(R.id.game_IMG_spray41),
                        findViewById(R.id.game_IMG_spray42)},
                {findViewById(R.id.game_IMG_cockroach0),
                        findViewById(R.id.game_IMG_cockroach1),
                        findViewById(R.id.game_IMG_cockroach2)}
        };
        game_BTN_right = findViewById(R.id.game_BTN_right);
        game_BTN_left = findViewById(R.id.game_BTN_left);
    }

}