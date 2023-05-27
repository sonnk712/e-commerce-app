package com.LTTBDD.ecommerce_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.activity.DetailActivity;
import com.LTTBDD.ecommerce_app.activity.OrderDetailActivity;
import com.LTTBDD.ecommerce_app.common.service.ItemClickListener;
import com.LTTBDD.ecommerce_app.common.utils.DateUtils;
import com.LTTBDD.ecommerce_app.dto.HistoryInfo;
import com.LTTBDD.ecommerce_app.model.Order;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private List<Order> list;
    private Context context;

    public OrderAdapter(List<Order> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order item = list.get(position);

        String id = Integer.toString(item.getId());
        Double totalPrice = item.getTotalPrice();
        String addressDelivery = item.getDeliveryAddress();
        String createdDate = DateUtils.dateToStringStartWithDDMMYYY(item.getCreatedDate());
        String status = "";
        if(item.getStatus() == 0){
            status = "Đã giao";
        }
        else{
            status = "Chưa giao";
        }
        DecimalFormat df = new DecimalFormat("###,###,###đ");
        holder.txtOrderId.setText(id);
        holder.txtOrderTotalPrice.setText(df.format(totalPrice));
        holder.txtOrderDelivery.setText(addressDelivery);
        holder.txtCreatedDate.setText(createdDate);
        holder.txtStatus.setText(status);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean isLongClick) {
                if(!isLongClick){
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    intent.putExtra("order", item);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                else{

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView txtOrderId, txtOrderTotalPrice, txtOrderDelivery, txtCreatedDate, txtStatus;
        ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.txtOrderId);
            txtOrderTotalPrice = itemView.findViewById(R.id.txtOrderTotalPrice);
            txtOrderDelivery = itemView.findViewById(R.id.txtOrderDelivery);
            txtCreatedDate = itemView.findViewById(R.id.txtCreatedDate);
            txtStatus = itemView.findViewById(R.id.txtStatus);
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
