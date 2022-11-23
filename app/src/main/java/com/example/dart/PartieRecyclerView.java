package com.example.dart;

public class PartieRecyclerView {
    private String name;
    private int set;
    private int leg;
    private int point;

    PartieRecyclerView(String name, int set, int leg, int point){
        this.name = name;
        this.set = set;
        this.leg = leg;
        this.point = point;
    }


    public String getName() {return name;}
    public int getSet() {return set;}
    public int getLeg() {return leg;}
    public int getPoint() {return point;}
    }

