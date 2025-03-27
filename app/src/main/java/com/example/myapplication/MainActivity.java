package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "mytag123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tv = findViewById(R.id.textView3);
        tv.setText("Welcome");

        Log.i(TAG, "onCreate: ");
        Log.e(TAG, "onCreate: sss");
        Log.i("TAG", "onCreate: ");

        EditText input = findViewById(R.id.inp);

        Button btn = findViewById(R.id.button2);
        btn.setOnClickListener(v -> {
            Log.i(TAG, "onClick: Button clicked");
            String inputstr = input.getText().toString();
            tv.setText("Welcome " + inputstr);
        });
    }

    public void myclick(View v) {
        Log.i(TAG, "myclick: hello");
        EditText input = findViewById(R.id.inp);
        String str = input.getText().toString();

        TextView out = findViewById(R.id.textView3);
        out.setText("Hello " + str);
    }
}