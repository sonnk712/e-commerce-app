package com.LTTBDD.ecommerce_app.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.adapter.OrderAdapter;
import com.LTTBDD.ecommerce_app.common.config.ConnectToMysql;
import com.LTTBDD.ecommerce_app.common.config.constants.Constants;
import com.LTTBDD.ecommerce_app.common.config.storage.DataLocalManager;
import com.LTTBDD.ecommerce_app.database.CartDatabase;
import com.LTTBDD.ecommerce_app.database.OrderDatabase;
import com.LTTBDD.ecommerce_app.model.Cart;
import com.LTTBDD.ecommerce_app.model.ItemProduct;
import com.LTTBDD.ecommerce_app.model.Order;
import com.LTTBDD.ecommerce_app.model.User;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.sql.Connection;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    RecyclerView recycleViewHistory;
    FrameLayout frameLayoutMain, framelayoutProfile;
    DrawerLayout drawerLayoutHistory;
    Toolbar toolbarHistory;
    NavigationView navHistory;
    NotificationBadge badge;
    User currUser;
    Cart cart;
    Connection conn;
    CartDatabase cartDatabase;
    OrderDatabase orderDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();
        actionToolBar();

        frameLayoutMain = findViewById(R.id.framelayoutMain);
        frameLayoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });

        framelayoutProfile = findViewById(R.id.framelayoutProfile);

    }

    private void initView(){
        recycleViewHistory = findViewById(R.id.recycleViewHistory);
        toolbarHistory = findViewById(R.id.toolbarHistory);
        drawerLayoutHistory = findViewById(R.id.drawerLayoutHistory);
        badge = findViewById(R.id.badge);
        navHistory = findViewById(R.id.navHistory);
        navHistory.setNavigationItemSelectedListener(this);

        conn = ConnectToMysql.connect();
        orderDatabase = new OrderDatabase(conn);
        currUser = DataLocalManager.getCurrUser();

        View hView = navHistory.inflateHeaderView(R.layout.layout_header_nav);
        ImageView currImg = (ImageView)hView.findViewById(R.id.currImage);
        TextView currName = (TextView)hView.findViewById(R.id.currName);
        TextView currEmail = (TextView)hView.findViewById(R.id.currEmail);

        if(TextUtils.isEmpty(currUser.getImg())){
            currImg.setImageResource(R.drawable.user_icon_2);
        }
        else{
            Uri uri = Uri.parse(currUser.getImg());
            Glide.with(getApplicationContext()).load(uri).into(currImg);
        }
        currName.setText(currUser.getName());
        currEmail.setText(currUser.getEmail());
        cartDatabase = new CartDatabase(conn);
        cart = cartDatabase.getCartFromUser(currUser.getUsername());
        List<ItemProduct> items = cartDatabase.getListItemByCart(cart.getId());
        badge.setText(Integer.toString(items.size()));

        navHistory.getMenu().findItem(R.id.nav_history).setChecked(true);

        List<Order> orders = orderDatabase.findOrderByUser(currUser.getId());
        if (orders == null){
            Toast.makeText(this, "Lỗi khi tải lịch sử", Toast.LENGTH_SHORT).show();
        }
        else if(orders.size() == 9){
            Toast.makeText(this, "Danh sách trống", Toast.LENGTH_SHORT).show();
        }

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recycleViewHistory.setLayoutManager(manager);
//        recycleViewHistory.setHasFixedSize(true);
        OrderAdapter adapter = new OrderAdapter(orders, getApplicationContext());
        recycleViewHistory.setAdapter(adapter);

    }

    private void actionToolBar(){
        setSupportActionBar(toolbarHistory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Lịch sử mua hàng");
        toolbarHistory.setTitleTextColor(getResources().getColor(R.color.white));
        toolbarHistory.setNavigationIcon(R.drawable.baseline_menu_24);
        toolbarHistory.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayoutHistory.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<ItemProduct> items = cartDatabase.getListItemByCart(cart.getId());
        badge.setText(Integer.toString(items.size()));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home){
            item.setChecked(true);
            redirectActivity(HistoryActivity.this, MainActivity.class);
        }
        else if(id == R.id.nav_mobile){
            item.setChecked(true);
            redirectActivity(HistoryActivity.this, MobileActivity.class);
        }
        else if(id == R.id.nav_laptop){
            item.setChecked(true);
            redirectActivity(HistoryActivity.this, LaptopActivity.class);
        } else if (id == R.id.nav_history) {
            item.setChecked(true);
            recreate();
        }
        else if(id == R.id.nav_search){
            redirectActivity(HistoryActivity.this, SearchActivity.class);
        }
        else if(id == R.id.nav_contact_us){
            item.setChecked(true);
            redirectActivity(HistoryActivity.this, ContactInforActivity.class);
        }
        else if(id == R.id.nav_about_us){
            item.setChecked(true);
            redirectActivity(HistoryActivity.this, AboutUsActivity.class);
        }
        else if(id == R.id.nav_myprofile){
            item.setChecked(true);
            redirectActivity(HistoryActivity.this, ProfileActivity.class);
        }
        else if(id == R.id.nav_wish_list){
            item.setChecked(true);
            redirectActivity(HistoryActivity.this, WishList.class);
        }
        closeDrawer(drawerLayoutHistory);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayoutHistory.isDrawerOpen(GravityCompat.START)){
            drawerLayoutHistory.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
    public static void redirectActivity(Activity currActivity, Class secondActivity){
        Intent intent = new Intent(currActivity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        currActivity.startActivity(intent);
        currActivity.finish();
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayoutHistory);
    }
}