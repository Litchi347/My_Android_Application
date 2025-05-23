package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RateManager {
    private static final String TAG = "RateManager";
    private DBHelper dbHelper;
    private String TBNAME;
    private Context context; // 添加上下文变量

    public RateManager(Context context) {
        this.context = context; // 保存上下文引用
        dbHelper = new DBHelper(context);
        TBNAME = DBHelper.TB_NAME;
    }
    public void add(RateItem item){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("CURNAME", item.getName());
        values.put("CURRATE", String.valueOf(item.getRate()));

        db.insert(TBNAME,null,values);
        db.close();
    }

    // 查询所有汇率数据的方法
    public ArrayList<RateItem> findAll(){
        ArrayList<RateItem> rateList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        if(cursor != null && cursor.getCount()>0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                String curName = cursor.getString(cursor.getColumnIndexOrThrow("CURNAME"));
                String curRate = cursor.getString(cursor.getColumnIndexOrThrow("CURRATE"));
                float rate = Float.parseFloat(curRate);

                rateList.add(new RateItem(curName, rate));
                cursor.moveToNext();
            }
            cursor.close();
        }

        db.close();
        return rateList;
    }

    // 批量添加汇率数据的方法
    public void addAll(ArrayList<RateItem> list) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 先清空原有数据
        db.delete(TBNAME, null, null);

        // 批量添加新数据
        for (RateItem item : list) {
            ContentValues values = new ContentValues();
            values.put("CURNAME", item.getName());
            values.put("CURRATE", String.valueOf(item.getRate()));
            db.insert(TBNAME, null, values);
        }

        // 保存最后更新日期
        saveLastUpdateDate();

        db.close();
    }

    // 保存最后更新日期的方法
    private void saveLastUpdateDate() {
        SharedPreferences sp = context.getSharedPreferences("rate_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        // 获取当前日期并格式化为yyyy-MM-dd格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());

        // 保存到SharedPreferences
        editor.putString("last_update_date", today);
        editor.apply();

        Log.i(TAG, "已保存最后更新日期: " + today);
    }

    // 检查今天是否已经更新过汇率
    public boolean isUpdatedToday() {
        SharedPreferences sp = context.getSharedPreferences("rate_prefs", Context.MODE_PRIVATE);
        String lastUpdateDate = sp.getString("last_update_date", "");

        // 获取今天的日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String today = sdf.format(new Date());

        // 判断是否为今天更新的
        boolean result = today.equals(lastUpdateDate);
        Log.i(TAG, "今天是否已更新: " + result + "，最后更新日期: " + lastUpdateDate);

        return result;
    }

}
