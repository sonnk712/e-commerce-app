package com.LTTBDD.ecommerce_app.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.common.config.ConnectToMysql;
import com.LTTBDD.ecommerce_app.common.config.storage.DataLocalManager;
import com.LTTBDD.ecommerce_app.common.service.ImageClickListener;
import com.LTTBDD.ecommerce_app.database.CartDatabase;
import com.LTTBDD.ecommerce_app.database.ProductData;
import com.LTTBDD.ecommerce_app.model.Cart;
import com.LTTBDD.ecommerce_app.model.EvenBus.ChangeTotalPriceEvent;
import com.LTTBDD.ecommerce_app.model.ItemProduct;
import com.LTTBDD.ecommerce_app.model.NewProduct;
import com.LTTBDD.ecommerce_app.model.User;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private List<ItemProduct> list;
    private Context context;

    public CartAdapter(List<ItemProduct> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_v2, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ItemProduct item = list.get(position);
        Connection conn = ConnectToMysql.connect();
        ProductData productData = new ProductData(conn);
        NewProduct data = productData.getProductById(item.getProductId());
        CartDatabase cartDatabase = new CartDatabase(conn);
//        User currUser = new User(1, "sonnk712@gmail.com", "123456", "sonefast712@gmail.com",
//                "Nguyễn Khắc Sơn", "Hạ Mỗ, Đan Phượng, Hà Nội", 1, new Date(), null, "0358785476", 1);
        User currUser = DataLocalManager.getCurrUser();
        Cart cart = cartDatabase.getCartFromUser(currUser.getUsername());


        holder.title.setText(data.getName());
        holder.quantity.setText(Integer.toString(item.getQuantity()));
        DecimalFormat df = new DecimalFormat("###,###,###đ");
        holder.price.setText(df.format(data.getPrice()));
        holder.totalPrice.setText(df.format(item.getTotalPrice()));
        Glide.with(context).load(data.getImage()).into(holder.img);
        holder.setImageClickListener(new ImageClickListener() {
            @Override
            public void onImageClick(View view, int pos, int value) {
                if(value == 1){
                    if(item.getQuantity() > 1){
                        item.setQuantity(item.getQuantity() - 1);
                        cart.setTotalQuantity(cart.getTotalQuantity() - 1);
                        cart.setTotalPrice(cart.getTotalPrice() - item.getPrice());
                    }

                }
                else if(value == 2){
                    if(item.getQuantity() < 10){
                        item.setQuantity(item.getQuantity() + 1);
                        cart.setTotalQuantity(cart.getTotalQuantity() + 1);
                        cart.setTotalPrice(cart.getTotalPrice() + item.getPrice());
                    }
                }
                else if(value == 3){
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                    builder.setTitle("Thông báo!");
                    builder.setMessage("Bạn có chắc chắc muốn xóa sản phẩm này khỏi giỏ hàng?");
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            cart.setTotalQuantity(cart.getTotalQuantity() - item.getQuantity());
                            cart.setTotalPrice(cart.getTotalPrice() - item.getPrice()* item.getQuantity());
                            cartDatabase.removeItem(item.getId());
                            cartDatabase.updateCartInfo(cart);
                            EventBus.getDefault().postSticky(new ChangeTotalPriceEvent());

                            Toast.makeText(context, "Đã xóa sản phẩm khỏi giỏ hàng", Toast.LENGTH_SHORT).show();
                            notifyDataSetChanged();
                            return;
                        }
                    });
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();


                }
                holder.quantity.setText(Integer.toString(item.getQuantity()));
                holder.totalPrice.setText(df.format(data.getPrice() * item.getQuantity()));

                cartDatabase.updateItemInfo(item);
                cartDatabase.updateCartInfo(cart);
                EventBus.getDefault().postSticky(new ChangeTotalPriceEvent());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        ImageView cartItemImage;
//        TextView itemCartName, itemCartPrice, itemCartQuantity, itemCartTotalPrice;
//        ImageView itemCartSub, itemCartAdd, removeItem;
        ImageClickListener imageClickListener;

        ImageView img, removeItem;
        TextView title, price, totalPrice, quantity, plusItem, minusItem;

        public void setImageClickListener(ImageClickListener imageClickListener) {
            this.imageClickListener = imageClickListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.textView);
            price = itemView.findViewById(R.id.textView9);
            totalPrice = itemView.findViewById(R.id.textView16);
            quantity = itemView.findViewById(R.id.textView23);
            plusItem = itemView.findViewById(R.id.textView24);
            minusItem = itemView.findViewById(R.id.textView17);
            removeItem = itemView.findViewById(R.id.btnRemove);
            plusItem.setOnClickListener(this);
            minusItem.setOnClickListener(this);
            removeItem.setOnClickListener(this);

//            cartItemImage = itemView.findViewById(R.id.cartItemImage);
//            itemCartName = itemView.findViewById(R.id.itemCartName);
//            itemCartPrice = itemView.findViewById(R.id.itemCartPrice);
//            itemCartQuantity = itemView.findViewById(R.id.itemCartQuantity);
//            itemCartTotalPrice = itemView.findViewById(R.id.itemCartTotalPrice);
//            itemCartSub = itemView.findViewById(R.id.itemCartSub);
//            itemCartAdd = itemView.findViewById(R.id.itemCartAdd);
//            removeItem = itemView.findViewById(R.id.removeItem);
//            itemCartSub.setOnClickListener(this);
//            itemCartAdd.setOnClickListener(this);
//            removeItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == minusItem){
                imageClickListener.onImageClick(view, getAdapterPosition(), 1);
            }
            else if(view == plusItem){
                imageClickListener.onImageClick(view, getAdapterPosition(), 2);
            }
            else if(view == removeItem){
                imageClickListener.onImageClick(view, getAdapterPosition(), 3);
            }
        }
    }
}
