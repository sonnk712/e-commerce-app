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
import android.widget.TextView;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.adapter.OrderDetailAdapter;
import com.LTTBDD.ecommerce_app.adapter.WishListAdapter;
import com.LTTBDD.ecommerce_app.common.config.ConnectToMysql;
import com.LTTBDD.ecommerce_app.common.config.storage.DataLocalManager;
import com.LTTBDD.ecommerce_app.database.CartDatabase;
import com.LTTBDD.ecommerce_app.database.OrderDatabase;
import com.LTTBDD.ecommerce_app.database.WishListDatabase;
import com.LTTBDD.ecommerce_app.model.Cart;
import com.LTTBDD.ecommerce_app.model.ItemProduct;
import com.LTTBDD.ecommerce_app.model.User;
import com.LTTBDD.ecommerce_app.model.WishListModel;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.sql.Connection;
import java.util.List;

public class WishList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    RecyclerView recycleViewWishList;
    FrameLayout frameLayoutMain, framelayoutProfile;
    DrawerLayout drawerLayoutWishList;
    Toolbar toolbarWishList;
    NavigationView navWishList;
    NotificationBadge badge;
    User currUser;
    Cart cart;
    Connection conn;
    CartDatabase cartDatabase;
    OrderDatabase orderDatabase;
    WishListDatabase wishListDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
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
        recycleViewWishList = findViewById(R.id.recycleViewWishList);
        toolbarWishList = findViewById(R.id.toolbarWishList);
        drawerLayoutWishList = findViewById(R.id.drawerLayoutWishList);
        badge = findViewById(R.id.badge);
        navWishList = findViewById(R.id.navWishList);
        navWishList.setNavigationItemSelectedListener(this);

        conn = ConnectToMysql.connect();
        orderDatabase = new OrderDatabase(conn);
        wishListDatabase = new WishListDatabase(conn);
        currUser = DataLocalManager.getCurrUser();

        View hView = navWishList.inflateHeaderView(R.layout.layout_header_nav);
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

        navWishList.getMenu().findItem(R.id.nav_wish_list).setChecked(true);

        List<WishListModel> list = wishListDatabase.getWishListFromUser(currUser.getId());
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recycleViewWishList.setLayoutManager(manager);
        WishListAdapter adapter = new WishListAdapter(list, getApplicationContext());
        recycleViewWishList.setAdapter(adapter);
    }

    private void actionToolBar(){
        setSupportActionBar(toolbarWishList);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Danh sách yêu thích");
        toolbarWishList.setTitleTextColor(getResources().getColor(R.color.white));
        toolbarWishList.setNavigationIcon(R.drawable.baseline_menu_24);
        toolbarWishList.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayoutWishList.openDrawer(GravityCompat.START);
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home){
            item.setChecked(true);
            redirectActivity(WishList.this, MainActivity.class);
        }
        else if(id == R.id.nav_mobile){
            item.setChecked(true);
            redirectActivity(WishList.this, MobileActivity.class);
        }
        else if(id == R.id.nav_laptop){
            item.setChecked(true);
            redirectActivity(WishList.this, LaptopActivity.class);
        } else if (id == R.id.nav_history) {
            item.setChecked(true);
            recreate();
        }
        else if(id == R.id.nav_search){
            redirectActivity(WishList.this, SearchActivity.class);
        }
        else if(id == R.id.nav_contact_us){
            item.setChecked(true);
            redirectActivity(WishList.this, ContactInforActivity.class);
        }
        else if(id == R.id.nav_about_us){
            item.setChecked(true);
            redirectActivity(WishList.this, AboutUsActivity.class);
        }
        else if(id == R.id.nav_myprofile){
            item.setChecked(true);
            redirectActivity(WishList.this, ProfileActivity.class);
        }
        else if(id == R.id.nav_wish_list){
            item.setChecked(true);
            recreate();
        }
        closeDrawer(drawerLayoutWishList);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<ItemProduct> items = cartDatabase.getListItemByCart(cart.getId());
        badge.setText(Integer.toString(items.size()));
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
        closeDrawer(drawerLayoutWishList);
    }
}