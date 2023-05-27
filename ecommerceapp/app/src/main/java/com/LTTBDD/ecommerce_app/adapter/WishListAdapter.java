package com.LTTBDD.ecommerce_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.common.service.ItemClickListener;

import com.LTTBDD.ecommerce_app.model.WishListModel;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.MyViewHolder> {
    private List<WishListModel> list;
    private Context context;

    public WishListAdapter(List<WishListModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wish_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        WishListModel item = list.get(position);
        holder.txtProductName.setText(item.getName());
        holder.txtProductId.setText("Mã: " + item.getId());
        DecimalFormat df = new DecimalFormat("###,###,###đ");
        holder.txtProductPrice.setText("Giá: " + df.format(item.getPrice()));
        holder.txtDescription.setText(item.getDescription());
        Glide.with(context).load(item.getImg()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtProductName, txtProductId, txtProductPrice , txtDescription;
        private ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.productNameWish);
            txtProductId = itemView.findViewById(R.id.productIdWish);
            txtProductPrice = itemView.findViewById(R.id.productPriceWish);
            txtDescription = itemView.findViewById(R.id.productDescriptionWish);
            image = itemView.findViewById(R.id.productImageWish);
        }
    }
}
