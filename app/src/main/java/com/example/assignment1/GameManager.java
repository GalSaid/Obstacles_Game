package com.example.assignment1;

import android.util.Log;

public class GameManager {

    private int currLives = 3;
    private int initialLives = 3; //for the reset game
    private int numOfLines; //included the player
    private int numOfRoutes;
    private Type[][] matrix;

    public GameManager(int numOfRoutes, int numOfLines, int initialLives) {
        if (initialLives < 1 || initialLives > 3) {
            currLives = initialLives;
            this.initialLives = initialLives;
        }
        this.numOfLines = numOfLines;
        this.numOfRoutes = numOfRoutes;
        matrix = new Type[numOfLines][numOfRoutes];
        resetGame();
    }

    public void addSpray() {
        int routeNum = (int) (Math.random() * numOfRoutes);
        matrix[0][routeNum] = Type.SPRAY;
    }

    public void updateMatrix() { //Lower the matrix row down
        for (int i = numOfLines - 2; i > 0; i--) {
            for (int j = 0; j < numOfRoutes; j++)
                matrix[i][j] = matrix[i - 1][j];
        }
        for (int i = 0; i < numOfRoutes; i++)
            matrix[0][i] = Type.EMPTY;
    }

    public boolean collisionExists() {
        for (int col = 0; col < numOfRoutes; col++) {
            if (matrix[numOfLines - 1][col] == Type.COCKROACH && matrix[numOfLines - 2][col] == Type.SPRAY)
                return true;
        }
        return false;
    }

    public void decreaseLive() {
        currLives--;
    }

    public int getCurrLives() {
        return currLives;
    }

    public int getCockroachCol() { //where is the player
        for (int i = 0; i < numOfRoutes; i++)
            if (matrix[numOfLines - 1][i] == Type.COCKROACH) {
                return i;
            }
        return -1;
    }

    public int setCockroachCol(int move) {
        int col = getCockroachCol();
        matrix[numOfLines - 1][col] = Type.EMPTY;
        matrix[numOfLines - 1][col + move] = Type.COCKROACH;
        return col + move;
    }

    public void setCurrLives(int lives) {
        currLives = lives;
    }

    public Type[][] getMatrix() {
        return matrix;
    }

    public void resetGame() { //after a loss the game is reset
        for (int i = 0; i < numOfLines; i++)
            for (int j = 0; j < numOfRoutes; j++) {
                matrix[i][j] = Type.EMPTY;
            }
        matrix[numOfLines - 1][numOfRoutes / 2] = Type.COCKROACH;
        setCurrLives(initialLives);
    }
}
