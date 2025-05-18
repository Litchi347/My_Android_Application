package com.example.myapplication;

import android.util.Log;

public class mytask implements Runnable {

    private static final String TAG = "MyTask";
    @Override
    public void run() {
        Log.i(TAG, "run: MyTask.");
    }
}
