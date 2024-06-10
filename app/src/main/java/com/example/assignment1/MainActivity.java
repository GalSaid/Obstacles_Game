package com.example.assignment1;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private AppCompatImageView[][] game_IMG_matrix;
    private AppCompatImageView[] game_IMG_hearts;
    private MaterialButton game_BTN_right;
    private MaterialButton game_BTN_left;
    private GameManager gameManager;
    private final Handler handler = new Handler();

    private int numOfRoutes=3;
    private int numOfLines=6;
    private int initialLives=3;
    private final int DELAY = 1000;
    private boolean addSpray; //add spary every 2 ticks


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        //Log.d("pttt", "start");
        addSpray=true;
        gameManager=new GameManager(numOfRoutes,numOfLines,initialLives);
        updateLivesUI();
        updateCockroachUI(numOfRoutes/2);
        updateSprayUI();
        game_BTN_left.setOnClickListener(v -> moveCockroach(true));
        game_BTN_right.setOnClickListener(v -> moveCockroach(false));
        start();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        //Log.d("pttt", "tick");
        start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }

    private void tick() {
        //Log.d("pttt", "tick");
        nextStep();

        //playSound();
        //count--;
    }


    private Runnable runnable = new Runnable() {
        public void run() {
            handler.postDelayed(runnable, DELAY);
//            if (count == 0) {
//                answered(false, false);
//                return;
//            }
            tick();

        }
    };

    private void stop() {
        handler.removeCallbacks(runnable);
    }

    private void start() {
        handler.postDelayed(runnable, DELAY);
    }

    private void moveCockroach(boolean  isLeft){
        int newPosition;
        if(isLeft && game_IMG_matrix[numOfLines-1][0].getVisibility()== View.VISIBLE)
            return;
        if(!isLeft && game_IMG_matrix[numOfLines-1][numOfRoutes-1].getVisibility()== View.VISIBLE)
            return;
        newPosition=gameManager.setCockroachCol(isLeft? -1:1);
        updateCockroachUI(newPosition);
    }
    private void nextStep(){
        //Log.d("pttt", "next step");
        if(gameManager.collisionExists())
        {
            gameManager.decreaseLive();
            updateLivesUI();
        }
        gameManager.updateMatrix();
        if(addSpray){
            gameManager.addSpray();
        }
        addSpray=!addSpray;
        updateSprayUI();
    }

    private void updateLivesUI(){
        int SZ = game_IMG_hearts.length;

        for (int i = 0; i < SZ; i++) {
            game_IMG_hearts[i].setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < SZ - gameManager.getLives(); i++) {
            game_IMG_hearts[SZ - i - 1].setVisibility(View.INVISIBLE);
        }
    }

    private void updateSprayUI(){
        Type[][] matrix =gameManager.getMatrix();
        for(int i=0; i<numOfLines-1; i++) {
            for (int j = 0; j < numOfRoutes; j++) {
                Log.d("pttt", matrix[i][j].toString());
            }
            Log.d("pttt", "newline");
        }
        for(int i=0; i<matrix.length-1;i++)
        {
            for(int j=0;j<matrix[i].length; j++){
                if(matrix[i][j]==Type.EMPTY)
                    game_IMG_matrix[i][j].setImageResource(0);
                else if(matrix[i][j]==Type.SPRAY)
                    game_IMG_matrix[i][j].setImageResource(R.drawable.img_spray);
            }
        }
    }

    private void printMatrix(Type mat[][])
    {
        for(int i=0; i<numOfLines-1; i++) {
            for (int j = 0; j < numOfRoutes; j++) {
                System.out.println(mat[i][j] + " "+"i");
            }
            System.out.println();
        }
    }
    private void updateCockroachUI(int location){
        for(int i=0; i<numOfRoutes;i++){
            if(i==location)
                game_IMG_matrix[numOfLines-1][i].setVisibility(View.VISIBLE);
            else
                game_IMG_matrix[numOfLines-1][i].setVisibility(View.INVISIBLE);
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
        game_BTN_right=findViewById(R.id.game_BTN_right);
        game_BTN_left=findViewById(R.id.game_BTN_left);
    }
}