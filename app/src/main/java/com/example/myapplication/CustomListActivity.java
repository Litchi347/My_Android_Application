package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = "CustomListActivity";
    Handler handler;
    private ListView mylist;
    ProgressBar progressBar;
    private MyAdapter adapter;

    private ArrayList<RateItem> rateItemList = new ArrayList<>(); // 数据源

    MyItemAdapter myItemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_custom_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });
        myItemAdapter = new MyItemAdapter(this, rateItemList);

        progressBar = findViewById(R.id.progressBar);

        ArrayList<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Rate: " + i);    // 标题文字
            map.put("ItemDetail", "detail" + i);    // 详细描述
            listItems.add(map);
        }

        ArrayList<RateItem> rateItems = new ArrayList<>();
        for (HashMap<String, String> map : listItems) {
            String title = map.get("ItemTitle");
            String detail = map.get("ItemDetail");
            try {
                // 从detail字符串中提取数字部分
                // 只保留数字和小数点
                String numberStr = detail.replaceAll("[^0-9.]", "");
                float rate = Float.parseFloat(numberStr);
                rateItemList.add(new RateItem(title, rate));
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing number from: " + detail);
                // 发生错误时使用默认值0
                rateItemList.add(new RateItem(title, 0.0f));
            }
        }

//        ArrayList<RateItem> rateItems = new ArrayList<>();
//        for (HashMap<String, String> map : listItems) {
//            String title = map.get("ItemTitle");
//            String detail = map.get("ItemDetail");
//            rateItemList.add(new RateItem(title, Float.parseFloat(detail)));
//        }


        // 生成适配器的 Item 和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,
                listItems,// listItems 数据源
                R.layout.list_item, // ListItem的XML布局实现
                new String[]{"ItemTitle", "ItemDetail"},
                new int[]{R.id.itemTitle, R.id.itemDetail}
        );


        mylist = findViewById(R.id.mylist2);
        mylist.setAdapter(myItemAdapter);

        mylist.setOnItemClickListener(this);    // 设置监听器，通过实现onItemClick方法来处理点击事件
        mylist.setOnItemLongClickListener(this);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                // 接收返回的数据项
                if (msg.what == 3) {
                    Log.i(TAG, "handleMessage: 获得网络数据");
//                    ArrayList<HashMap<String, String>> list2 = (ArrayList<HashMap<String, String>>) msg.obj;
//                    ArrayList<RateItem> newRateItems = new ArrayList<>();
//                    for (HashMap<String,String> map:list2){
//                        String title = map.get("ItemTitle");
//                        String detail = map.get("ItemDetail");
//                        try{
//                            float rate = Float.parseFloat(detail);
//                            newRateItems.add(new RateItem(title, rate));
//                        }catch (NumberFormatException e){
//                            Log.e(TAG, "handleMessage: 解析数字错误", e);
//                            newRateItems.add(new RateItem(title, 0.0f));
//                        }
//                    }
//                    MyAdapter adapter2 = new MyAdapter(CustomListActivity.this, R.layout.list_item, list2);
//                    SimpleAdapter listItemAdapter = new SimpleAdapter(CustomListActivity.this, listItems, R.layout.list_item, // ListItem 的XML布局实现
//                            new String[]{"ItemTitle", "ItemDetail"},
//                            new int[]{R.id.itemTitle, R.id.itemDetail}
//                    );
//                    mylist.setAdapter(adapter2);
//                    mylist.setOnItemClickListener(CustomListActivity.this);
                    ArrayList<RateItem> newRateItems = (ArrayList<RateItem>) msg.obj;

                    rateItemList.clear();
                    rateItemList.addAll(newRateItems);
                    myItemAdapter.notifyDataSetChanged(); // 通知适配器数据已更改

                    // 保存到数据库
                    RateManager manager = new RateManager(CustomListActivity.this);
                    manager.addAll(newRateItems); // 将数据写入数据库

                    // 隐藏进度条
                    progressBar.setVisibility(View.GONE);
                    // 将所有的数据写入数据库
                }

                super.handleMessage(msg);
            }
        };
//        
//        // 向数据库中写入数据
//        RateManager manager = new RateManager(this);
//        RateItem item = new RateItem("欧元2",34.5f);
//        manager.add(item);
//        Log.i(TAG, "onCreate: 写入数据库结束");

        RateManager manager = new RateManager(this);
        if (manager.isUpdatedToday()) {
            // 今天已更新过，直接从数据库读取
            Log.i(TAG, "onCreate: 今天已经更新过汇率，从数据库获取数据");
            progressBar.setVisibility(View.GONE);

            // 从数据库读取数据
            ArrayList<RateItem> dbRates = manager.findAll();

            if (!dbRates.isEmpty()) {
                rateItemList.clear();
                rateItemList.addAll(dbRates);
                myItemAdapter.notifyDataSetChanged(); // 通知适配器数据已更改
                Log.i(TAG, "onCreate: 已从数据库加载" + dbRates.size() + "条汇率数据");
            } else {
                Log.i(TAG, "onCreate: 数据库中无数据，将从网络中获取");
                fetchDataFromNetwork();
            }
        } else {
            // 今天尚未更新，需要从网络中获取数据
            Log.i(TAG, "onCreate: 今天尚未更新汇率，从网络中获取数据");
            fetchDataFromNetwork();
        }
    }
    
    // 从网络中获取数据的方法
    private void fetchDataFromNetwork(){
        progressBar.setVisibility(View.VISIBLE);

        // 启动线程
        WebItemTask task = new WebItemTask();
        task.setHandler(handler);
        Thread t = new Thread(task);
        t.start();
        Log.i(TAG, "fetchDataFromNetwork: 开始从网络中获取汇率数据");
//
//        new Thread(() -> {
//            Document doc = null;
//            ArrayList<HashMap<String,String>> retlist = new ArrayList<HashMap<String,String>>();
//            try {
//                doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();
//                Log.i(TAG, "run: title=" + doc.title());
//
//                Elements tables = doc.getElementsByTag("Table");
//                Element table = tables.get(1); // 获取第二个表格
//                Elements trs = table.getElementsByTag("tr"); // 获取所有的 tr 元素
//                trs.remove(0); // 去掉表头
//
//                for (Element tr : trs) {
//                    Elements tds = tr.children();
//                    Element td1 = tds.first();
//                    Element td2 = tds.get(5);
//                    String str1 = td1.text();
//                    String str2 = td2.text();
//
//                    Log.i(TAG, "run: " + str1 + " => " + str2);
//                    HashMap<String, String> map = new HashMap<String, String>();
//                    map.put("ItemTitle", str1);
//                    map.put("ItemDetail", str2);
//                    retlist.add(map);
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            Message msg = handler.obtainMessage(3, retlist);
//            handler.sendMessage(msg);
//            Log.i(TAG, "onCreate: Handler.sengMessage(msg)");
//        }).start();

    }
    

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.i(TAG, "onItemClick: ");
//        Object itemAtPosition = mylist.getItemAtPosition(position);
//        HashMap<String, String> map = (HashMap<String, String>) itemAtPosition;
//        String titleStr = map.get("ItemTitle");
//        String detailStr = map.get("ItemDetail");

        RateItem item = (RateItem) mylist.getItemAtPosition(position);
        String titleStr = item.getName();
        String detailStr = String.valueOf(item.getRate());
        Log.i(TAG, "onItemClick: titleStr=" + titleStr);
        Log.i(TAG, "onItemClick: detailStr=" + detailStr);

        // 打开新的窗口
//        Intent intent = new Intent(CustomListActivity.this, RateCalcActivity.class);
//        Bundle bundle = new Bundle();
//        intent.putExtra("currency", titleStr);
//        intent.putExtra("rate", detailStr);
////        bundle.putString("currency", titleStr);
////        bundle.putString("rate", detailStr);
////        intent.putExtras(bundle);
//        startActivity(intent);

        // 删除数据项
//        adapter.remove((HashMap<String, String>) mylist.getItemAtPosition(position));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
            .setMessage("请确认是否删除当前数据")
            .setPositiveButton("是",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i(TAG, "onClick: 对话框事件处理");
                    rateItemList.remove(position); // 删除当前项
                    myItemAdapter.notifyDataSetChanged(); // 通知适配器数据已更改
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "onClick: 取消删除");
                        dialog.dismiss(); // 关闭对话框
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);  // 允许点击外部关闭
        dialog.setCanceledOnTouchOutside(true);  // 允许触摸外部关闭
        dialog.show();
    }

    // 处理长按事件
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.i(TAG, "onItemLongClick: ");
        return true;
    }
}
