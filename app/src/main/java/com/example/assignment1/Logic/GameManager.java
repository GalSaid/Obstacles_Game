package com.example.assignment1.Logic;

import com.example.assignment1.Utilities.Type;

public class GameManager {

    private int currLives = 3;
    private int initialLives = 3; //for the reset game
    private int score = 0;
    private int numOfLines; //included the player
    private int numOfRoutes;
    private int cockroachCol;
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

    public void addItem() { //add spray or garbage
        int Item =(int) (Math.random() * 2);
        int routeNum = (int) (Math.random() * numOfRoutes);
        matrix[0][routeNum] = Item==0? Type.SPRAY: Type.GARBAGE;
    }

    public int getScore(){
        return score;
    }

    public void updateMatrix() { //Lower the matrix row down
        for (int i = numOfLines - 2; i > 0; i--) {
            for (int j = 0; j < numOfRoutes; j++)
                matrix[i][j] = matrix[i - 1][j];
        }
        for (int i = 0; i < numOfRoutes; i++)
            matrix[0][i] = Type.EMPTY;
    }

    public boolean negativeCollisionExists() {
        for (int col = 0; col < numOfRoutes; col++) {
            if (matrix[numOfLines - 1][col] == Type.COCKROACH && matrix[numOfLines - 2][col] == Type.SPRAY)
                return true;
        }
        return false;
    }

    public boolean positiveCollisionExists() {
        for (int col = 0; col < numOfRoutes; col++) {
            if (matrix[numOfLines - 1][col] == Type.COCKROACH && matrix[numOfLines - 2][col] == Type.GARBAGE){
                score+=5;
                return true;
            }
        }
        return false;
    }
    public void decreaseLive() {
        currLives--;
    }

    public int getCurrLives() {
        return currLives;
    }


    public int getCockroachCol(){
        return cockroachCol;
    }

    public void setCockroachCol(int move) {
        int col = getCockroachCol();
        matrix[numOfLines - 1][col] = Type.EMPTY;
        matrix[numOfLines - 1][col + move] = Type.COCKROACH;
       cockroachCol+=move;
    }

    public void setCurrLives(int lives) {
        currLives = lives;
    }

    public Type[][] getMatrix() {
        return matrix;
    }

    public void resetGame() { //after a loss the game is reset
        cockroachCol=numOfRoutes / 2;
        for (int i = 0; i < numOfLines; i++)
            for (int j = 0; j < numOfRoutes; j++) {
                matrix[i][j] = Type.EMPTY;
            }
        matrix[numOfLines - 1][cockroachCol] = Type.COCKROACH;
        setCurrLives(initialLives);
    }
}
