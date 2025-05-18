package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
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
        etAmount = findViewById(R.id.etAmount);
        btnCalculate = findViewById(R.id.btnCalculate);
        tvResult = findViewById(R.id.tvResult);

        // 获取从前一个Activity传递的数据
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("currency");
            detail = bundle.getString("rate");

            // 显示币种和汇率信息
            tvCurrency.setText(title);
            tvRate.setText(detail);

            // 解析汇率值
            try {
                String rateStr = detail.replace("汇率", "").trim();
                rate = Double.parseDouble(rateStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // 设置计算按钮点击监听器
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateExchange();
            }
        });
    }

    // 计算汇率转换
    private void calculateExchange() {
        String amountStr = etAmount.getText().toString();
        if (!amountStr.isEmpty() && rate > 0) {
            try {
                double amount = Double.parseDouble(amountStr);
                double result = amount * rate;
                tvResult.setText(String.format("%.2f", result));
            } catch (NumberFormatException e) {
                tvResult.setText("请输入有效数字");
            }
        } else {
            tvResult.setText("请输入金额");
        }
    }
}