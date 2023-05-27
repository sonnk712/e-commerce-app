package com.LTTBDD.ecommerce_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.activity.DetailActivity;
import com.LTTBDD.ecommerce_app.common.config.ConnectToMysql;
import com.LTTBDD.ecommerce_app.common.config.storage.DataLocalManager;
import com.LTTBDD.ecommerce_app.common.service.ImageClickListener;
import com.LTTBDD.ecommerce_app.common.service.ItemClickListener;
import com.LTTBDD.ecommerce_app.database.CartDatabase;
import com.LTTBDD.ecommerce_app.database.ProductData;
import com.LTTBDD.ecommerce_app.database.WishListDatabase;
import com.LTTBDD.ecommerce_app.model.Cart;
import com.LTTBDD.ecommerce_app.model.ItemProduct;
import com.LTTBDD.ecommerce_app.model.NewProduct;
import com.LTTBDD.ecommerce_app.model.User;
import com.LTTBDD.ecommerce_app.model.WishListModel;
import com.bumptech.glide.Glide;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class NewProductAdapter extends RecyclerView.Adapter<NewProductAdapter.MyViewHolder> {
    private List<NewProduct> list;
    private Context context;

    public NewProductAdapter(List<NewProduct> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_newproduct, parent, false);

        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Connection conn = ConnectToMysql.connect();
        CartDatabase cartDatabase = new CartDatabase(conn);
        WishListDatabase wishListDatabase = new WishListDatabase(conn);
        User currUser = DataLocalManager.getCurrUser();
        Cart cart = cartDatabase.getCartFromUser(currUser.getUsername());

        NewProduct item = list.get(position);
        holder.name.setText(item.getName());
        DecimalFormat df = new DecimalFormat("###,###,###đ");
        holder.price.setText("Giá: " + df.format(item.getPrice()));
        Glide.with(context).load(item.getImage()).into(holder.image);


        holder.setItemClickListener(new ItemClickListener() {
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

//        holder.setImageClickListener(new ImageClickListener() {
//            @Override
//            public void onImageClick(View view, int pos, int value) {
//                if(value == 1){
//
//                    boolean isSuccess = true;
//                    ItemProduct itemProduct = cartDatabase.checkProductExisted(item.getId(), cart.getId());
//                    if(itemProduct != null){
//                        itemProduct.setQuantity(itemProduct.getQuantity() + 1);
//                        itemProduct.setPrice(item.getPrice());
//                        itemProduct.setTotalPrice(itemProduct.getTotalPrice() + item.getPrice());
//                        Boolean checkExecute = cartDatabase.updateItemInfo(itemProduct);
//                        if(!checkExecute){
//                            isSuccess = false;
//                            return;
//                        }
//                    }
//                    else{
//                        itemProduct = new ItemProduct();
//                        itemProduct.setQuantity(1);
//                        itemProduct.setPrice(item.getPrice());
//                        itemProduct.setTotalPrice(item.getPrice());
//                        itemProduct.setCartId(cart.getId());
//                        itemProduct.setProductId(item.getId());
//
//                        Boolean checkExecute = cartDatabase.create(itemProduct, cart.getId());
//                        if(!checkExecute){
//                            isSuccess = false;
//                            return;
//                        }
//                    }
//
//                    cart.setTotalPrice(cart.getTotalPrice() + item.getPrice());
//                    cart.setTotalQuantity(cart.getTotalQuantity() + 1);
//                    Boolean checkExecute = cartDatabase.updateCartInfo(cart);
//                    if(!checkExecute){
//                        isSuccess = false;
//                        return;
//                    }
//
//                    if(isSuccess){
//                        Toast.makeText(context.getApplicationContext(), "Đã thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        Toast.makeText(context.getApplicationContext(), "Thêm sản phẩm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else if(value == 2){
//                    Boolean checkExist = wishListDatabase.checkWishlistExist(item.getId(), currUser.getId());
//                    if(checkExist == true){
//                        Toast.makeText(context, "Sản phẩm này đã có trong danh sách yêu thích", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    else{
//
//                    }
//                    WishListModel data = new WishListModel();
//                    data.setId(item.getId());
//                    data.setName(item.getName());
//                    data.setPrice(item.getPrice());
//                    data.setImg(item.getImage());
//                    data.setDescription(item.getDescription());
//                    data.setUserId(currUser.getId());
//                    Boolean check = wishListDatabase.create(data);
//                    if(check){
//                        Toast.makeText(context, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        Toast.makeText(context, "Xảy ra lỗi khi thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView name, price;
        ImageView image, addtoCart, addToWishList;
        ItemClickListener itemClickListener;

        ImageClickListener imageClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.newproduct_name);
            price = itemView.findViewById(R.id.newproduct_price);
            image = itemView.findViewById(R.id.newproduct_image);
            addtoCart = itemView.findViewById(R.id.addToCart);
            addToWishList = itemView.findViewById(R.id.addToWishList);
            itemView.setOnClickListener(this);
            addtoCart.setOnClickListener(this);
            addToWishList.setOnClickListener(this);
        }

        public void setImageClickListener(ImageClickListener imageClickListener) {
            this.imageClickListener = imageClickListener;
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
//
//            if(view == addtoCart){
//                imageClickListener.onImageClick(view, getAdapterPosition(), 1);
//            }
//            else if(view == addToWishList){
//                imageClickListener.onImageClick(view, getAdapterPosition(), 2);
//            }
        }
    }
}
