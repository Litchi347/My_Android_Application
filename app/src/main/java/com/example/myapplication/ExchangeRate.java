package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ExchangeRate extends AppCompatActivity implements Runnable{
    private static final String TAG = "ExchangeRateActivity";    // 调试日志标记符，为当前类定义一个统一的标签，方便在日志中查找
    private EditText input_rmb;
    private TextView tv_result;

    private float dollarRate = 7.31f;
    private float wonRate = 198.26f;
    private float yenRate = 19.84f;

    Handler handler;    // 定义了一个Handler对象，用于处理线程间的消息传递，特别是子线程向主线程发送消息的情况

    // 原始作用：初始化界面和组件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);    // 更好地支持沉浸式状态栏
        setContentView(R.layout.activity_exchange_rate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);    // 设置主视图的padding,使内容不会被遮挡
            return insets;
        });

        // 将xml中的输入框和结果文本框与Java变量进行绑定，后续可以读取输入内容和显示结果
        input_rmb = findViewById(R.id.inp_change);
        tv_result = findViewById(R.id.result);

        // 从本地存储中读取保存的数据 load from sp
        // 可以实现在app每次启动时都能读取到上次保存的数据
//        SharedPreferences sp = getSharedPreferences("myrate",ExchangeRate.MODE_PRIVATE);    // 获取名为myrate的SharedPreferences文件，相当于一个小型数据库
//        dollarRate = sp.getFloat("sp_dollar_key",1.23f);
//        wonRate = sp.getFloat("sp_won_key",2.34f);
//        yenRate = sp.getFloat("sp_yen_key",3.45f);
//        Log.i(TAG, "onCreate: from sp dollarRate=" + dollarRate);
//        Log.i(TAG, "onCreate: from sp wonRate=" + wonRate);
//        Log.i(TAG, "onCreate: from sp yenRate=" + yenRate);

        // 创建Handler子类对象，用于在子线程中运行完任务后，向主线程中发送消息
        // 创建一个新的Handler对象，重写handleMessage方法
        handler = new Handler(){
            // 当子线程使用handler对象发送消息时，这个方法就会在主线程中被调用
            @Override
            public void handleMessage(@NonNull Message msg){    // msg就是子线程发送的消息
                Log.i(TAG, "HandleMessage: 接收消息");
                if (msg.what == 7){    // msg.what是发送消息时设置的标志，类似于一个标识符
                    Bundle bdl = (Bundle) msg.obj;    // msg.obj是发送消息时设置的对象

                    float dollar = bdl.getFloat("web_dollar");
                    float won = bdl.getFloat("web_won");
                    float yen = bdl.getFloat("web_yen");

                    Log.i(TAG, "handleMessage: str="+ bdl);
//                    tv_result.setText("dollar" + "won" + "yen");    // 更新界面
                }
                super.handleMessage(msg);    // 调用父类的handleMessage方法
            }
        };

        // 启动一个新线程来执行耗时操作，以避免阻塞主线程
        // thread
        Log.i(TAG, "onCreate: 启动线程");

        Thread t = new Thread(this);    // 创建了一个新的线程对象t
                                               // this表示当前类ExchangeRate实现了Runnable接口 => activity类中应该含有run()方法
                                               // 这个线程启动后，会自动调用当前类的run()方法
        t.start();    // this.run => 启动线程调用run方法

        // 线程的不同启动方法
//        Thread t2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.i(TAG, "run: Thread2 running");
//            }
//        });
//        t2.start();
//
//        Thread t3 = new Thread(()->{
//            Log.i(TAG, "run: Thread3 running");
//        });
//        t3.start();
//
//        new Thread(()->{
//            Log.i(TAG, "run: Thread4 running");
//        }).start();
//
//        mytask task = new mytask();
//        Thread t5 = new Thread(task);
//        // Thread t5 = new Thread(new MyTask());
//        t5.start();
//        // new Thread(new MyTask()).start();

        // save to sp
        SharedPreferences sp = getSharedPreferences("myrate",ExchangeRate.MODE_PRIVATE);    // 获取一个名为myrate的SharedPreferences对象，用于存储本地数据。ExchangeRate.MODE_PRIVATE表示只有当前应用可以访问这个SharedPreferences文件
                                                                                                  // sp变量相当于一个本地的数据仓库
        SharedPreferences.Editor editor = sp.edit();    // 用于向SharedPreferences文件中写入数据
        editor.putFloat("sp_dollar_key",dollarRate);
        editor.putFloat("sp_won_key",wonRate);
        editor.putFloat("sp_yen_key",yenRate);
        editor.apply();    // 提交修改，保存数据到本地存储
        Log.i(TAG, "onCreate: save to sp");

    }

    public void myclick (View btn){
        Log.i(TAG, " myclick:change ");
        String StrInput = input_rmb.getText().toString();

        try {
            // 计算 输入x计算汇率result
            float inputf = Float.parseFloat(StrInput);
            float result = 0;
            if (btn.getId() == R.id.button_dollar_label) {
                result = inputf * dollarRate;
            }

            else if (btn.getId() == R.id.button_won_label) {
                result = inputf * wonRate;
            }

            else if (btn.getId() == R.id.button_yen_label){
                result = inputf * yenRate;
            }

            // 显示结果
            tv_result.setText(String.valueOf(result));
        }
        catch (NumberFormatException e) {
            // tv_result.setText("请输入正确的数据");
            Toast.makeText(this,"请输入正确的数据",Toast.LENGTH_SHORT).show();
        }
    }

    // 当用户点击某个按钮时，打开一个新的页面，并把汇率数据传递过去
    public void openConfig(View btn){
        // 打开新窗口
        Intent intent = new Intent(this,change_to.class);    // 表示从当前activity跳转到change_to这个activity
        intent.putExtra("key_dollar",dollarRate);
        intent.putExtra("key_won",wonRate);
        intent.putExtra("key_yen",yenRate);

        // startActivity(intent);    // 最基础的页面跳转方式，但不能处理跳转后的返回结果
        // 使用ActivityResultLauncher启动Activity
        luncher.launch(intent);
    }

    // 调用t.start()后，自动调用run方法
    @Override
    public void run() {
        Bundle retbdl = new Bundle();
        Log.i(TAG, "run: running........");


        try {
            Thread.sleep(2000);

            URL url = null;
            try {
//                url = new URL("https://www.swufe.edu.cn/info/1068/36061.hyml");
//                HttpURLConnection http = (HttpURLConnection) url.openConnection();
//                InputStream in = http.getInputStream();
//
//                String html = inputStream2String(in);
//                Log.i(TAG, "run: html=" + html);

                // 访问网页并获取其html文档
                Document doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();
                Log.i(TAG, "run: title=" + doc.title());

                // 提取网页中的表格和数据
                Elements tables = doc.getElementsByTag("table");
                Element table = tables.get(1);    // 获取第二个表格
                Elements trs = table.getElementsByTag("tr");    // 获取所有的tr元素

                trs.remove(0);
                for(Element tr : trs){

                    Elements tds = tr.children();
                    Element td1 = tds.first();
                    Element td2 = tds.get(5);

                    String str1 = td1.text();
                    String str2 = td2.text();
                    Log.i(TAG, "run: " + str1 + "=>" + str2);

                    try {
                        float r = 100 / Float.parseFloat(str2);
                        if("美元".equals(str1)){
                            retbdl.putFloat("web_dollar",r);
                        }else if("韩国元".equals(str1)) {
                            retbdl.putFloat("web_won", r);
                        }else if("日元".equals(str1)){
                            retbdl.putFloat("web_yen", r);
                        }
                    } catch (NumberFormatException e) {
                        throw new RuntimeException(e);
                    }
                }

            } catch (
                    MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 向主线程发送消息
        Message msg = handler.obtainMessage();    // 在子线程中获取一个消息对象
        msg.what = 7;
        msg.obj = retbdl;    // obj是消息可以携带的任意对象数据
        handler.sendMessage(msg);    // 通过handler发送消息
        Log.i(TAG, "run:" + "dollar rate = " + retbdl.getFloat("web_dollar"));
        Log.i(TAG, "run:" + "won rate = " + retbdl.getFloat("web_won"));
        Log.i(TAG, "run:" + "yen rate = " + retbdl.getFloat("web_yen"));
        Log.i(TAG, "run: 消息发送完毕");
    }
//
//    private String inputStream2String(InputStream inputStream)
//            throws IOException{
//        final int buffer_size = 1024;
//        final char[] buffer = new char[buffer_size];
//        final StringBuilder out = new StringBuilder();
//        Reader in = new InputStreamReader(inputStream,"gb2312");
//        while(true){
//            int rsz = in.read(buffer,0,buffer.length);
//            if (rsz < 0){
//                break;
//            }
//            out.append(buffer,0,rsz);
//        }
//        return out.toString();
//    }
//

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (resultCode == 3 && requestCode == 6){
//            // 从data获取带回的数据
//            Bundle ret = data.getExtras();
//
//            dollarRate = ret.getFloat("ret_dollar",dollarRate);
//            wonRate = ret.getFloat("ret_won",wonRate);
//            yenRate = ret.getFloat("ret_yen",yenRate);
//
//            Log.i(TAG, "onActivityResult:dollarRate= " + dollarRate);
//            Log.i(TAG, "onActivityResult:wonRate= " + wonRate);
//            Log.i(TAG, "onActivityResult:yenRate= " + yenRate);
//            }
//        super.onActivityResult(requestCode, resultCode, data);
//        }
//    }

    ActivityResultLauncher luncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),    // 指定跳转方式，使用标准的"启动一个activity并获取返回结果“模式
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {    // 当用户从新页面返回后，代码自动执行
                    // intent用于页面跳转和数据传递，bundle用于数据的封装和传递
                    Intent data = result.getData();    // 获取返回的数据
                    Bundle bundle = data.getExtras();    // 获取返回的Bundle对象

                    if(bundle != null){
                        dollarRate = bundle.getFloat("rate_dollar",dollarRate);
                        wonRate = bundle.getFloat("rate_won",wonRate);
                        yenRate = bundle.getFloat("rate_dollar",yenRate);

                        Log.i(TAG, "onActivityResult:dollarRate= " + dollarRate);
                        Log.i(TAG, "onActivityResult:wonRate= " + wonRate);
                        Log.i(TAG, "onActivityResult:yenRate= " + yenRate);
                    }
                }
            });
}