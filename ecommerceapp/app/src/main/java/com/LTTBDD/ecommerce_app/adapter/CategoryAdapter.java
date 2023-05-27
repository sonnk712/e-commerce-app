package com.LTTBDD.ecommerce_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.model.Category;
import com.bumptech.glide.Glide;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    List<Category> list;
    Context context;

    public CategoryAdapter(List<Category> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class ViewHolder{
        TextView itemName;
        ImageView itemImage;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.item_product, null);
            viewHolder.itemName = view.findViewById(R.id.itemName);
            viewHolder.itemImage = view.findViewById(R.id.itemImage);
            view.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) view.getTag();

        }

        viewHolder.itemName.setText(list.get(i).getName());
        Glide.with(context).load(list.get(i).getImage()).into(viewHolder.itemImage);

        return view;
    }
}
