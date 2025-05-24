package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MyItemAdapter extends ArrayAdapter<RateItem> {
    private final Context context;
    private final ArrayList<RateItem> items;

    public MyItemAdapter(Context context, ArrayList<RateItem> items) {
        super(context, R.layout.list_item, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        RateItem item = (RateItem) getItem(position);

        TextView title = itemView.findViewById(R.id.itemTitle);
        TextView detail = itemView.findViewById(R.id.itemDetail);

        title.setText("币种：" + item.get("ItemTitle"));
        detail.setText("汇率：" + item.get("ItemDetail"));

        return itemView;
    }
}
