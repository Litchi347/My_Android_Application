package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter extends ArrayAdapter<HashMap<String, String>> {
    private int res;

    public MyAdapter(@NonNull Context context, int resource, @NonNull ArrayList<HashMap<String,String>> objects) {
        super(context, resource, objects);
        this.res = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View itemView = convertView;
        if(itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(this.res, parent, false);
        }

//        HashMap<String, String> map = getItem(position);
        Map<String,String> map = (Map<String,String>) getItem(position);
        TextView title = itemView.findViewById(R.id.itemTitle);
        TextView detail = itemView.findViewById(R.id.itemDetail);

        title.setText("币种：" + map.get("ItemTitle"));
        detail.setText("汇率:" + map.get("ItemDetail"));

//        itemView.setOnClickListener(v -> {
//            Intent intent = new Intent(getContext(), RateCalcActivity.class);
//            intent.putExtra("currency", map.get("ItemTitle"));
//            intent.putExtra("rate", map.get("ItemDetail"));
//            getContext().startActivity(intent);
//        });

        return itemView;
    }
}
