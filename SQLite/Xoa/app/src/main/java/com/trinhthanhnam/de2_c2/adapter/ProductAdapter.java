package com.trinhthanhnam.de2_c2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.trinhthanhnam.de2_c2.MainActivity;
import com.trinhthanhnam.de2_c2.R;
import com.trinhthanhnam.de2_c2.model.Product;

import java.util.List;

public class ProductAdapter extends BaseAdapter {

    MainActivity context;
    int layout_item;

    List<Product> products;

    public ProductAdapter(MainActivity context, int layout_item, List<Product> products) {
        this.context = context;
        this.layout_item = layout_item;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Object getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout_item, null);
            holder.txtInfor =convertView.findViewById(R.id.txtInfor);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();

        }

        //binding data
        Product p = products.get(position);
        holder.txtInfor.setText(p.getProductName());
        return convertView;
    }

    public static class ViewHolder{
        TextView txtInfor;


    }
}
