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

import java.text.DecimalFormat;

public class MainActivity1 extends AppCompatActivity {
        private static final String TAG = "bmi_cal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tv = findViewById(R.id.textView);
        TextView tv1 = findViewById(R.id.textView1);
        EditText input1 = findViewById(R.id.inp1);
        EditText input2 = findViewById(R.id.inp2);
        Button btn = findViewById(R.id.button2);

        tv.setText("健康生活");
        Log.i(TAG, "onCreate: ");
        Log.e(TAG, "onCreate: sss");
        Log.i("TAG", "onCreate: ");
        btn.setOnClickListener(v -> {
            Log.i(TAG, "onClick: Button clicked");

            String inputstr1 = input1.getText().toString();
            String inputstr2 = input2.getText().toString();

            try {
                float height = Float.parseFloat(inputstr1);
                float weight = Float.parseFloat(inputstr2);

                float bmi = weight / (height * height);
                DecimalFormat df = new DecimalFormat("#0.00");
                String result = "BMI: " + df.format(bmi) + "\n";
                tv.setText(result);

                if (bmi < 18.5) {
                    result += "You are too thin";
                } else if (bmi >= 18.5 && bmi <= 23.9) {
                    result += "Perfect!";
                } else if (bmi >= 24 && bmi <= 27.9) {
                    result += "Keep moving";
                } else {
                    result += "Please eat less";
                }
                tv1.setText(result);
            } catch (NumberFormatException e) {
                tv1.setText("请输入完整数据后进行");
            }
        });
    }

    public void myclick(View v) {
        Log.i(TAG, "myclick: hello");
        EditText input = findViewById(R.id.inp);
        String str = input.getText().toString();

        TextView out = findViewById(R.id.textView3);
        out.setText("Hello " + str);
    }
    // 添加了一行内容
}