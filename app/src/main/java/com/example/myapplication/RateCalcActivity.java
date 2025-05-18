package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RateCalcActivity extends AppCompatActivity {

    private TextView tvCurrency;
    private TextView tvRate;
    private EditText etAmount;
    private Button btnCalculate;
    private TextView tvResult;
    private String title;
    private String detail;
    private double rate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rate_calc);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化视图
        tvCurrency = findViewById(R.id.tvCurrency);
        tvRate = findViewById(R.id.tvRate);

        // 获取从前一个Activity传递的数据
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            title = bundle.getString("currency");
//            detail = bundle.getString("rate");
//        }
        title = getIntent().getStringExtra("currency");
        detail = getIntent().getStringExtra("rate");

        // 显示币种和汇率信息
        tvCurrency.setText(title);
        tvRate.setText(detail);

    }

    // 设置计算按钮点击监听器
//        btnCalculate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                calculateExchange();
//            }
//        });
//    }

}