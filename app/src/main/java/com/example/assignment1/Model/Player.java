package com.example.assignment1.Model;

public class Player implements Comparable<Player> {
    private String name;
    private int score;
    private double lat = 0.0;
    private double lng = 0.0;

    public Player(String name,int score){
        this.name=name;
        this.score=score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public int compareTo(Player other) {
        return Integer.compare(other.getScore(), this.getScore()); // Sort from highest to lowest
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
