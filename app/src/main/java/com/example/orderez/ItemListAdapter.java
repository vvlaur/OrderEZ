package com.example.orderez;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ItemListAdapter extends ArrayAdapter implements AdapterView.OnItemClickListener {

    private Context context;
    private List list;
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
    }

    class ViewHolder {
        public TextView list_name;
    }

    public ItemListAdapter(@NonNull Context context, ArrayList list) {
        super(context,0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.itemlist_listview_item, parent, false);
        }
        viewHolder = new ViewHolder();
        viewHolder.list_name = (TextView) convertView.findViewById(R.id.itemlistCategoryText);
        final itemList setList = (itemList) list.get(position);

        viewHolder.list_name.setText(setList.getCategory());
        return convertView;
    }

}