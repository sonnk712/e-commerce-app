package com.LTTBDD.ecommerce_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.common.config.ConnectToMysql;
import com.LTTBDD.ecommerce_app.common.config.storage.DataLocalManager;
import com.LTTBDD.ecommerce_app.database.CartDatabase;
import com.LTTBDD.ecommerce_app.model.Cart;
import com.LTTBDD.ecommerce_app.model.EvenBus.ChangeTotalPriceEvent;
import com.LTTBDD.ecommerce_app.model.ItemProduct;
import com.LTTBDD.ecommerce_app.model.NewProduct;
import com.LTTBDD.ecommerce_app.model.User;
import com.bumptech.glide.Glide;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    TextView nameDetail, priceDetail, descDetail;
    ImageView imageDetail;
    Spinner spinnerDetail;
    FrameLayout frameLayoutCart;
    Button btnAddToCart;

    Toolbar toolbarDetail;
    NewProduct data;

    User currUser;
    Cart cart;
    NotificationBadge badge;

    Connection conn;

    CartDatabase cartDatabase;
    List<ItemProduct> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        actionToolBar();
        loadData();
        loadControl();
    }

    private void loadControl(){
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currUser != null){
                    addItemToCart();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    android.app.AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
                    builder.setTitle("Thông báo!");
                    builder.setMessage("Bạn cần phải đăng nhập để thực hiện chức năng này. Bạn có muốn đăng nhập không?");
                    builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(intent);
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
            }
        });
    }

    private void addItemToCart(){
        ItemProduct item = cartDatabase.checkProductExisted(data.getId(), cart.getId());
        boolean isSuccess = true;
        int quantity = Integer.parseInt(spinnerDetail.getSelectedItem().toString());
        Double price = data.getPrice();
        if(item != null){
            item.setQuantity(item.getQuantity() + quantity);
            item.setPrice(price);
            item.setTotalPrice(item.getTotalPrice() + price * quantity);
            Boolean checkExecute = cartDatabase.updateItemInfo(item);
            if(!checkExecute){
                isSuccess = false;
                return;
            }
        }
        else{
            item = new ItemProduct();
            item.setQuantity(quantity);
            item.setPrice(price);
            item.setTotalPrice(quantity * price);
            item.setCartId(cart.getId());
            item.setProductId(data.getId());

            Boolean checkExecute = cartDatabase.create(item, cart.getId());
            if(!checkExecute){
                isSuccess = false;
                return;
            }
        }

        cart.setTotalPrice(cart.getTotalPrice() + price * quantity);
        cart.setTotalQuantity(cart.getTotalQuantity() + quantity);
        Boolean checkExecute = cartDatabase.updateCartInfo(cart);
        if(!checkExecute){
            isSuccess = false;
            return;
        }


        if(isSuccess){
            Toast.makeText(getApplicationContext(), "Đã thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Thêm sản phẩm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
        }
        items = cartDatabase.getListItemByCart(cart.getId());
        badge.setText(Integer.toString(items.size()));
    }

    private void loadData(){
        data = (NewProduct) getIntent().getSerializableExtra("data");
//        currUser = (User) getIntent().getSerializableExtra("currUser");
        //fake
//        currUser = new User(1, "sonnk712@gmail.com", "123456", "sonefast712@gmail.com",
//                "Nguyễn Khắc Sơn", "Hạ Mỗ, Đan Phượng, Hà Nội", 1, new Date(), null, "0358785476", 1);

        currUser = DataLocalManager.getCurrUser();
        nameDetail.setText(data.getName());
        descDetail.setText("Mô tả: \n" + data.getDescription().trim());
        DecimalFormat df = new DecimalFormat("###,###,###đ");
        priceDetail.setText("Giá: " + df.format(data.getPrice()));
        Glide.with(getApplicationContext()).load(data.getImage()).into(imageDetail);
        Integer[] spinnerData = new Integer[]{1, 2, 3, 4, 5};
        ArrayAdapter<Integer> spinnerAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.
                layout.support_simple_spinner_dropdown_item, spinnerData);
        spinnerDetail.setAdapter(spinnerAdapter);
        conn = ConnectToMysql.connect();;
        cartDatabase = new CartDatabase(conn);
        cart = cartDatabase.getCartFromUser(currUser.getUsername());
        items = cartDatabase.getListItemByCart(cart.getId());
        badge.setText(Integer.toString(items.size()));
    }
    private void initView(){
        nameDetail = findViewById(R.id.nameDetail);
        priceDetail = findViewById(R.id.priceDetail);
        descDetail = findViewById(R.id.descDetail);
        imageDetail = findViewById(R.id.imageDetail);
        spinnerDetail = findViewById(R.id.spinnerDetail);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        toolbarDetail = findViewById(R.id.toolbarDetail);
        badge = findViewById(R.id.badge);
        frameLayoutCart = findViewById(R.id.framelayoutCart);
        frameLayoutCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });

    }

    private void actionToolBar(){
        setSupportActionBar(toolbarDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}