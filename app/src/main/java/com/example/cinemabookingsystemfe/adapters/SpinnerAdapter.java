package com.example.cinemabookingsystemfe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cinemabookingsystemfe.R;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    private Context context;
    private List<String> items;
    private LayoutInflater inflater;

    public SpinnerAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // This is for the selected item (closed spinner)
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_item_with_arrow, parent, false);
        }
        
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(items.get(position));
        
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // This is for the dropdown list
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_dropdown_item, parent, false);
        }
        
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(items.get(position));
        
        return convertView;
    }
}
