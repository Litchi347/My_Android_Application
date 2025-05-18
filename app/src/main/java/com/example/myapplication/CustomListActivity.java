package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
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
import java.util.ArrayList;
import java.util.HashMap;

public class CustomListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "CustomListActivity";
    Handler handler;
    private ListView mylist;

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

        ArrayList<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Rate: " + i);
            map.put("ItemDetail", "detail" + i);
            listItems.add(map);
        }

        // 生成适配器的 Item 和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,
                listItems,// listItems 数据源
                R.layout.list_item, // ListItem的XML布局实现
                new String[]{"ItemTitle", "ItemDetail"},
                new int[]{R.id.itemTitle, R.id.itemDetail}
        );

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                // 接收返回的数据项
                if (msg.what == 3) {
                    Log.i(TAG, "handleMessage: 获得网络数据");
                    ArrayList<HashMap<String, String>> list2 = (ArrayList<HashMap<String, String>>) msg.obj;
                    MyAdapter adapter2 = new MyAdapter(CustomListActivity.this, R.layout.list_item, list2);
//                    SimpleAdapter listItemAdapter = new SimpleAdapter(CustomListActivity.this, listItems, R.layout.list_item, // ListItem 的XML布局实现
//                            new String[]{"ItemTitle", "ItemDetail"},
//                            new int[]{R.id.itemTitle, R.id.itemDetail}
//                    );
                    mylist.setAdapter(adapter2);
//                    mylist.setOnItemClickListener(CustomListActivity.this);

                }

                super.handleMessage(msg);
            }
        };

        MyAdapter myAdapter = new MyAdapter(this, R.layout.list_item, listItems);

        mylist = findViewById(R.id.mylist2);
        mylist.setAdapter(myAdapter);
        mylist.setOnItemClickListener(this);

//        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.i(TAG, "onItemClick: 2222222"+i);
//                Object itemAtPosition = mylist.getItemAtPosition(i);
//                HashMap<String, String> map = (HashMap<String, String>) itemAtPosition;
//                String titleStr = map.get("ItemTitle");
//                String detailStr = map.get("ItemDetail");
//                Log.i(TAG, "onItemClick: titleStr=" + titleStr);
//                Log.i(TAG, "onItemClick: detailStr=" + detailStr);
//            }
//        });

        // 启动线程
        new Thread(() -> {
            Document doc = null;
            ArrayList<HashMap<String,String>> retlist = new ArrayList<HashMap<String,String>>();
            try {
                doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();
                Log.i(TAG, "run: title=" + doc.title());

                Elements tables = doc.getElementsByTag("Table");
                Element table = tables.get(1); // 获取第二个表格
                Elements trs = table.getElementsByTag("tr"); // 获取所有的 tr 元素
                trs.remove(0); // 去掉表头

                for (Element tr : trs) {
                    Elements tds = tr.children();
                    Element td1 = tds.first();
                    Element td2 = tds.get(5);
                    String str1 = td1.text();
                    String str2 = td2.text();

                    Log.i(TAG, "run: " + str1 + " => " + str2);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("ItemTitle", str1);
                    map.put("ItemDetail", str2);
                    retlist.add(map);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Message msg = handler.obtainMessage(3, retlist);
            handler.sendMessage(msg);
            Log.i(TAG, "onCreate: Handler.sengMessage(msg)");
        }).start();

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.i(TAG, "onItemClick: ");
        Object itemAtPosition = mylist.getItemAtPosition(position);
        HashMap<String, String> map = (HashMap<String, String>) itemAtPosition;
        String titleStr = map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");
        Log.i(TAG, "onItemClick: titleStr=" + titleStr);
        Log.i(TAG, "onItemClick: detailStr=" + detailStr);

        // 打开新的窗口
        Intent intent = new Intent(CustomListActivity.this, RateCalcActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtra("currency", titleStr);
        intent.putExtra("rate", detailStr);
//        bundle.putString("currency", titleStr);
//        bundle.putString("rate", detailStr);
//        intent.putExtras(bundle);
        startActivity(intent);



//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("提示")
//        .setMessage("请确认是否删除当前数据")
//                .setPositiveButton("是",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Log.i(TAG, "onClick: 对话框事件处理");
//                        myAdapter.remove((HashMap<String,String>)mylist.getItemAtPosition(position)); // 删除当前项
////
////                        // 删除当前项
////                        ListItems.remove(position);
////                        // 更新适配器
////                        listItemAdapter.notifyDataSetChanged();
//                    }
//                }).setNegativeButton("否",null);
//        builder.create().show();
//


    }
}
//
//    @Override
//    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
//        Log.i(TAG, "onItemLongClick: ");
//        return true;
//    }
//}
