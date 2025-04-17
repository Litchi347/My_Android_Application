package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CourtCounter extends AppCompatActivity {

    private static final String TAG = "CourtActivity";
    private TextView scoring2;
    private TextView scoring3;

    int ints1;
    int ints2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_court_counter);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scoring2=findViewById(R.id.score_a);
        scoring3=findViewById(R.id.score_b);
    }

    public void a_click(View btn){
        Log.i(TAG,"click:a");
        String s1 = (String) scoring2.getText();
        ints1 = Integer.parseInt(s1);
        if (btn.getId()==R.id.button_a1){
            ints1 ++;
        }
        else if(btn.getId()==R.id.button_a2){
            ints1 += 2;
        }
        else if(btn.getId()==R.id.button_a3){
            ints1 += 3;
        }
        scoring2.setText(String.valueOf(ints1));
    }

    public void b_click(View btn){
        Log.i(TAG,"click:b");
        String s2 = (String) scoring3.getText();
        ints2 = Integer.parseInt(s2);
        if (btn.getId()==R.id.button_b1){
            ints2 ++;
        }
        else if(btn.getId()==R.id.button_b2){
            ints2 += 2;
        }
        else if(btn.getId()==R.id.button_b3){
            ints2 += 3;
        }
        scoring3.setText(String.valueOf(ints2));
    }

    public void click(View btn){
        Log.i(TAG, "click: reset");
        if (btn.getId()==R.id.button_reset){
            ints1 = 0;
            ints2 = 0;
        }
        scoring2.setText(String.valueOf(ints1));
        scoring3.setText(String.valueOf(ints2));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("key1",ints1);
        outState.putInt("key2",ints2);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ints1 = savedInstanceState.getInt("key1");
        ints2 = savedInstanceState.getInt("key2");

        scoring2.setText(String.valueOf(ints1));
        scoring3.setText(String.valueOf(ints2));
    }

}