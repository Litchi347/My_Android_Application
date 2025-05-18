package com.example.myapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable {

    private static final String TAG = "RateListActivity";
    Handler handler;
    List<String> datalist = new ArrayList<>(); // 存储从网页获取的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datalist);
//        setListAdapter(adapter);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 7) {

                    List<String> newData = (List<String>) msg.obj; // 从 msg.obj 获取数据
                    datalist.clear();
                    datalist.addAll(newData);

                    ListAdapter adapter2 = new ArrayAdapter<>(RateListActivity.this, android.R.layout.simple_list_item_1, datalist);
                    setListAdapter(adapter2);
                }
            }
        };

        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        List<String> fetchedData = new ArrayList<>(); // 用于存储从网页解析的数据

        try {
            // 使用 Jsoup 访问网页并获取其 HTML 文档
            Document doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();
            Log.i(TAG, "run: title=" + doc.title());

            // 提取网页中的表格和数据
            Elements tables = doc.getElementsByTag("table");
            Element table = tables.get(1); // 获取第二个表格
            Elements trs = table.getElementsByTag("tr"); // 获取所有的 tr 元素

            trs.remove(0); // 去掉表头
            for (Element tr : trs) {
                Elements tds = tr.children();
                Element td1 = tds.first(); // 货币名
                Element td2 = tds.get(5); // 现汇卖出价

                String currencyName = td1.text();
                String exchangeRate = td2.text();
                fetchedData.add(currencyName + " => " + exchangeRate); // 格式化数据

                Log.i(TAG, "run: " + currencyName + " => " + exchangeRate);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 将数据发送到主线程
        Message msg = handler.obtainMessage();
        msg.what = 7;
        msg.obj = fetchedData; // 将解析后的数据存储到 msg.obj 中
        handler.sendMessage(msg);
    }
}