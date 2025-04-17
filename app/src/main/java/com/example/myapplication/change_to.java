package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class change_to extends AppCompatActivity {

    private static final String TAG = "Change_to";

    private TextView dollar_rate;
    private TextView won_rate;
    private TextView yen_rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_to);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        float dollar = getIntent().getFloatExtra("key_dollar",0.1f);
        float won = getIntent().getFloatExtra("key_won",0.1f);
        float yen = getIntent().getFloatExtra("key_yen",0.1f);

        Log.i(TAG, "onCreate: dollar=" + dollar);
        Log.i(TAG, "onCreate: won=" + won);
        Log.i(TAG, "onCreate: yen=" + yen);

        dollar_rate = findViewById(R.id.new_dollar);
        won_rate = findViewById(R.id.new_won);
        yen_rate = findViewById(R.id.new_yen);

        // 在控件中显示传入的数据
        dollar_rate.setText(String.valueOf(dollar));
        won_rate.setText(String.valueOf(won));
        yen_rate.setText(String.valueOf(yen));

    }

    public void save(View btn){
        Log.i(TAG, "save");
        String dollarStr = dollar_rate.getText().toString();
        String wonStr = won_rate.getText().toString();
        String yenStr = yen_rate.getText().toString();

        Log.i(TAG, "save: dollarStr=" + dollarStr);
        Log.i(TAG, "save: wonStr=" + wonStr);
        Log.i(TAG, "save: yenStr=" + yenStr);

        // 转String -> float
        try {
            float dollar = Float.parseFloat(dollarStr);
            float won = Float.parseFloat(wonStr);
            float yen = Float.parseFloat(yenStr);

            // 带回数据
            Intent retIntent = getIntent();
            Bundle bdl = new Bundle();
            bdl.putFloat("ret_dollar",dollar);
            bdl.putFloat("ret_win",won);
            bdl.putFloat("ret_yen",yen);
            retIntent.putExtras(bdl);
            setResult(3,retIntent);

            // 结束当前活动
            finish();
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }
}