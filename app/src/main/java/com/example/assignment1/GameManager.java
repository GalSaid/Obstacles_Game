package com.example.assignment1;

import android.util.Log;

import java.util.ArrayList;

public class GameManager {

    private int lives = 3;
    private int numOfLines;//included the cockroach's row
    private int numOfRoutes;
    private Type[][] matrix;

    public GameManager(int numOfRoutes,int numOfLines, int initialLives) {
        if (initialLives < 0  ||  initialLives >3 ) {
            lives = initialLives;
        }
        this.numOfLines=numOfLines;
        this.numOfRoutes=numOfRoutes;
        matrix = new Type[numOfLines][numOfRoutes];
        resetGame();
    }

    public void addSpray(){
        int routeNum= (int) (Math.random() * numOfRoutes);
        Log.d("pttt", String.valueOf(routeNum));
        matrix[0][routeNum]=Type.SPRAY;
    }

    public void updateMatrix(){
        for(int i=numOfLines-2; i>0 ;i--){
            for(int j=0; j<numOfRoutes; j++)
                matrix[i][j]=matrix[i-1][j];
        }
        for(int i=0; i<numOfRoutes; i++)
            matrix[0][i]=Type.EMPTY;
    }
    public boolean collisionExists(){
        for(int col=0; col<numOfRoutes; col++) {
            if (matrix[numOfLines - 1][col] == Type.COCKROACH && matrix[numOfLines - 2][col] == Type.SPRAY)
                return true;
        }
        return false;
    }

    public void decreaseLive() {
        lives--;
    }

    public int getLives() {
        return lives;
    }

    public int getCockroachCol(){
        for(int i=0; i<numOfRoutes; i++)
            if(matrix[numOfLines-1][i]==Type.COCKROACH){
                //Log.d("pttt", String.valueOf(i)+" found");
                return i;
            }
        return -1;
    }

    public int setCockroachCol(int move){
        int col=getCockroachCol();
        matrix[numOfLines-1][col]=Type.EMPTY;
        matrix[numOfLines-1][col+move]=Type.COCKROACH;
        return col+move;
    }


    public Type[][] getMatrix(){
        return matrix;
    }

    public void addExtraLive() {
        lives++;
    }

    public void resetGame(){
        for(int i=0; i<numOfLines; i++)
            for(int j=0;j<numOfRoutes; j++)
            {
                matrix[i][j]=Type.EMPTY;
            }
        matrix[numOfLines-1][numOfRoutes/2]=Type.COCKROACH;
    }
}
