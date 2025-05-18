package com.example.myapplication;

public class RateItem {
    private String name;
    private float rate;


    public RateItem(String name, float rate) {
        this.name = name;
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public float getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return "RateItem{" +
                "name='" + name + '\'' +
                ", rate='" + rate + '\'' +
                '}';
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
