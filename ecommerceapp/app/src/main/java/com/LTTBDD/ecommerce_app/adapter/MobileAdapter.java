package com.LTTBDD.ecommerce_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.activity.DetailActivity;
import com.LTTBDD.ecommerce_app.common.service.ItemClickListener;
import com.LTTBDD.ecommerce_app.model.NewProduct;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

public class MobileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewProduct> list;
    private Context context;

    private static final int VIEW_TYPE_DATA = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    public MobileAdapter(List<NewProduct> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATA){
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mobile, parent, false);
            return new MyViewHolder(item);
        }
        else{
            View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(item);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder viewHolder = (MyViewHolder) holder;
            NewProduct item = list.get(position);
            viewHolder.name.setText(item.getName().trim());
//            viewHolder.id.setText(Integer.toString(item.getId()));
            DecimalFormat df = new DecimalFormat("###,###,###đ");
            viewHolder.price.setText("Giá: " + df.format(item.getPrice()));
//            String briefDesc[] = item.getDescription().split("\n");
//            String desc = briefDesc + "\n" + briefDesc[1] + "\n" + briefDesc[2] + " ...";
//            String briefDesc = item.getDescription().substring(0, 32) + " ...";
            viewHolder.desc.setText("Mô tả: \n" + item.getDescription().trim());
            Glide.with(context).load(item.getImage()).into(viewHolder.image);
            viewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int pos, boolean isLongClick) {
                    if(!isLongClick){
                        Intent intent = new Intent(context, DetailActivity.class);
                        intent.putExtra("data", item);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    else{

                    }
                }
            });
        }
        else{
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView id, name, price, desc;
        ImageView image;
        ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
//            id = itemView.findViewById(R.id.mobile_id);
            name = itemView.findViewById(R.id.mobile_name);
            price = itemView.findViewById(R.id.mobile_price);
            image = itemView.findViewById(R.id.mobile_image);
            desc = itemView.findViewById(R.id.mobile_desc);
            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }
}
