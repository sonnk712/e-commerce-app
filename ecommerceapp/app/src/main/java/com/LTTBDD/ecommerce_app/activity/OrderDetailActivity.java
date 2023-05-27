package com.LTTBDD.ecommerce_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.adapter.OrderDetailAdapter;
import com.LTTBDD.ecommerce_app.common.config.ConnectToMysql;
import com.LTTBDD.ecommerce_app.common.config.storage.DataLocalManager;
import com.LTTBDD.ecommerce_app.common.utils.DateUtils;
import com.LTTBDD.ecommerce_app.database.CartDatabase;
import com.LTTBDD.ecommerce_app.database.OrderDatabase;
import com.LTTBDD.ecommerce_app.dto.HistoryInfo;
import com.LTTBDD.ecommerce_app.model.Cart;
import com.LTTBDD.ecommerce_app.model.ItemProduct;
import com.LTTBDD.ecommerce_app.model.NewProduct;
import com.LTTBDD.ecommerce_app.model.Order;
import com.LTTBDD.ecommerce_app.model.User;
import com.bumptech.glide.Glide;
import com.nex3z.notificationbadge.NotificationBadge;

import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    TextView txtOrderDetailId, txtOrderDetaiLPrice, txtOrderDetailAddress, txtOrderDetailStatus, txtCreatedDateOrderDetail;
    Toolbar toolbarOrderDetail;
    RecyclerView recyclerViewOrderDetail;
    User currUser;
    Cart cart;
    Connection conn;
    CartDatabase cartDatabase;
    List<ItemProduct> items;
    OrderDatabase orderDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
        loadData();
        actionToolBar();
    }
    private void initView(){
        toolbarOrderDetail = findViewById(R.id.toolbarOrderDetail);
        recyclerViewOrderDetail = findViewById(R.id.recycleViewOrderDetail);
        txtOrderDetailId = findViewById(R.id.txtOrderDetailId);
        txtOrderDetaiLPrice = findViewById(R.id.txtOrderDetaiLPrice);
        txtOrderDetailAddress = findViewById(R.id.txtOrderDetailAddress);
        txtOrderDetailStatus = findViewById(R.id.txtOrderDetailStatus);
        txtCreatedDateOrderDetail = findViewById(R.id.txtCreatedDateOrderDetail);

    }

    private void loadData(){
        Intent intent = getIntent();
//        int orderId = intent.getIntExtra("orderId", 0);
        Order order = (Order) intent.getSerializableExtra("order");

        txtOrderDetailId.setText(Integer.toString(order.getId()));
        DecimalFormat df = new DecimalFormat("###,###,###đ");
        txtOrderDetaiLPrice.setText(df.format(order.getTotalPrice()));
        txtOrderDetailAddress.setText(order.getDeliveryAddress());
        txtCreatedDateOrderDetail.setText(DateUtils.dateToStringStartWithDDMMYYY(order.getCreatedDate()));

        if(order.getStatus() == 0){
            txtOrderDetailStatus.setText("Đã giao");
        }
        else{
            txtOrderDetailStatus.setText("Chưa giao");
        }
        currUser = DataLocalManager.getCurrUser();
        conn = ConnectToMysql.connect();;
        cartDatabase = new CartDatabase(conn);
        orderDatabase = new OrderDatabase(conn);
        cart = cartDatabase.getCartFromUser(currUser.getUsername());
        items = cartDatabase.getListItemByCart(cart.getId());
//        badge.setText(Integer.toString(items.size()));

        List<HistoryInfo> historyInfos = orderDatabase.findOrderDetailByOrder(order.getId());
        if(historyInfos != null){
            RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
            recyclerViewOrderDetail.setLayoutManager(manager);
            OrderDetailAdapter adapter = new OrderDetailAdapter(historyInfos, getApplicationContext());
            recyclerViewOrderDetail.setAdapter(adapter);
        }

    }

    private void actionToolBar(){
        setSupportActionBar(toolbarOrderDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thông tin chi tiết đơn hàng");
        toolbarOrderDetail.setTitleTextColor(getResources().getColor(R.color.white));
        toolbarOrderDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}