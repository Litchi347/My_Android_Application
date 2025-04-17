package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ScoringActivity extends AppCompatActivity {
    private static final String TAG = "ScroingActivity";
    private TextView scoring1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_scoring);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        scoring1=findViewById(R.id.s1);
    }

    public void click(View btn){
        Log.i(TAG,"click:11");
        // 获取原有的分数
        String s = (String) scoring1.getText();

        // String -> int
        int ints = Integer.parseInt(s);

        if (btn.getId()==R.id.button6){
            ints ++;
        }
        if (btn.getId()==R.id.button7){
            ints +=2;
        }
        if (btn.getId()==R.id.button8){
            ints +=3;
        }
        if (btn.getId()==R.id.button9){
            ints = 0;
        }
        scoring1.setText(String.valueOf(ints));
    }
}