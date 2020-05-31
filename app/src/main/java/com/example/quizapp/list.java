package com.example.quizapp;

import android.os.Parcel;
import android.os.Parcelable;

public class list {
    private String name;
    private String score;

    public list() {}

    public list(String name, String score) {
        this.name = name;
        this.score = score;

    }

    public String getName() {
        return name;
    }

    public String getScore() {
        return score;
    }
}

