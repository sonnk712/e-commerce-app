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
import com.LTTBDD.ecommerce_app.dto.HistoryInfo;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder>{
    private List<HistoryInfo> list;
    private Context context;

    public OrderDetailAdapter(List<HistoryInfo> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_detail, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailAdapter.MyViewHolder holder, int position) {
        HistoryInfo item = list.get(position);
        holder.txtProductName.setText(item.getName());
        holder.txtProductId.setText("Mã: " + item.getId());
        DecimalFormat df = new DecimalFormat("###,###,###đ");
        holder.txtProductPrice.setText("Giá: " + df.format(item.getPrice()));
        holder.txtDescription.setText(item.getDescription());
        holder.txtQuantity.setText("Số lượng: " + item.getQuantity());
        Glide.with(context).load(item.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView txtProductName, txtProductId, txtProductPrice, txtQuantity, txtDescription;
        private ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.productName);
            txtProductId = itemView.findViewById(R.id.productId);
            txtProductPrice = itemView.findViewById(R.id.productPrice);
            txtQuantity = itemView.findViewById(R.id.quantity);
            txtDescription = itemView.findViewById(R.id.productDescription);
            image = itemView.findViewById(R.id.productImage1);
        }
    }
}
