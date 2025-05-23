package com.example.myapplication;

public class RateItem {
    private int id;
    private String name;
    private float rate;

    public RateItem(){

    }
    public RateItem(String name, float rate) {
        this.name = name;
        this.rate = rate;
    }

    public RateItem(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public float getRate() {
        return rate;
    }

    public String get(String itemTitle) {
        if (itemTitle.equals("ItemTitle")) {
            return name;
        } else if (itemTitle.equals("ItemDetail")) {
            return String.valueOf(rate);
        }
        return null;
    }
}
