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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.LTTBDD.ecommerce_app.R;
import com.LTTBDD.ecommerce_app.adapter.MobileAdapter;
import com.LTTBDD.ecommerce_app.common.config.ConnectToMysql;
import com.LTTBDD.ecommerce_app.common.config.constants.Constants;
import com.LTTBDD.ecommerce_app.common.config.constants.MessageCode;
import com.LTTBDD.ecommerce_app.common.config.dto.ServiceResponse;
import com.LTTBDD.ecommerce_app.common.config.storage.DataLocalManager;
import com.LTTBDD.ecommerce_app.database.CartDatabase;
import com.LTTBDD.ecommerce_app.database.OrderDatabase;
import com.LTTBDD.ecommerce_app.database.ProductData;
import com.LTTBDD.ecommerce_app.model.Cart;
import com.LTTBDD.ecommerce_app.model.ItemProduct;
import com.LTTBDD.ecommerce_app.model.NewProduct;
import com.LTTBDD.ecommerce_app.model.User;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class LaptopActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    Toolbar toolbarMobile;
    RecyclerView recycleViewMobile;

    LinearLayoutManager layoutManager;

    List<NewProduct> productDataList;
    NavigationView navMobile;
    DrawerLayout drawerLayoutMobile;
    MobileAdapter adapter;
    int page = 1, category = 2;

    FrameLayout frameLayoutMain, framelayoutProfile;
    NotificationBadge badge;
    User currUser;
    Cart cart;
    Connection conn;
    CartDatabase cartDatabase;
    OrderDatabase orderDatabase;
    Handler handler = new Handler();
    boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laptop);
        initView();
        actionToolBar();
        getListMobile();
        addEventLoad();
    }

    private void initView() {
        frameLayoutMain = findViewById(R.id.framelayoutMain);
        frameLayoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cart = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(cart);
            }
        });
        toolbarMobile = findViewById(R.id.toolbarMobile);
        recycleViewMobile = findViewById(R.id.recycleViewMobile);
        navMobile = findViewById(R.id.navMobile);
        drawerLayoutMobile = findViewById(R.id.drawerLayoutMobile);
        badge = findViewById(R.id.badge);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycleViewMobile.setLayoutManager(layoutManager);
        recycleViewMobile.setHasFixedSize(true);
        productDataList = new ArrayList<>();

        navMobile.setNavigationItemSelectedListener(this);
        navMobile.getMenu().findItem(R.id.nav_laptop).setChecked(true);

        conn = ConnectToMysql.connect();
        orderDatabase = new OrderDatabase(conn);
        currUser = DataLocalManager.getCurrUser();

        View hView = navMobile.inflateHeaderView(R.layout.layout_header_nav);
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
    }

    @Override
    public void onBackPressed() {
        if (drawerLayoutMobile.isDrawerOpen(GravityCompat.START)) {
            drawerLayoutMobile.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        if (id == R.id.nav_home) {
            item.setChecked(true);
            redirectActivity(LaptopActivity.this, MainActivity.class);
        } else if (id == R.id.navMobile) {
            item.setChecked(true);
            redirectActivity(LaptopActivity.this, MobileActivity.class);
        } else if (id == R.id.nav_laptop) {
            item.setChecked(true);
            restartActivity(this);
        } else if (id == R.id.nav_history) {
            item.setChecked(true);
            redirectActivity(LaptopActivity.this, HistoryActivity.class);
        } else if(id == R.id.nav_signout){
            DataLocalManager.logout();
            Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(logout);
        }
        else if(id == R.id.nav_search){
            redirectActivity(LaptopActivity.this, SearchActivity.class);
        }
        else if(id == R.id.nav_contact_us){
            item.setChecked(true);
            redirectActivity(LaptopActivity.this, ContactInforActivity.class);
        }
        else if(id == R.id.nav_about_us){
            item.setChecked(true);
            redirectActivity(LaptopActivity.this, AboutUsActivity.class);
        }
        else if(id == R.id.nav_myprofile){
            item.setChecked(true);
            redirectActivity(LaptopActivity.this, ProfileActivity.class);
        }
        else if(id == R.id.nav_wish_list){
            item.setChecked(true);
            redirectActivity(LaptopActivity.this, WishList.class);
        }
        closeDrawer(drawerLayoutMobile);
        return true;
    }

    private void addEventLoad() {
        recycleViewMobile.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoading == false) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == productDataList.size() - 1) {
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                productDataList.add(null);
                adapter.notifyItemChanged(productDataList.size() - 1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                productDataList.remove(productDataList.size() - 1);
                adapter.notifyItemRemoved(productDataList.size());
                page += 1;
                getListMobile();
                adapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    private void getListMobile() {
        try {
            Connection conn = ConnectToMysql.connect();
            ProductData productData = new ProductData(conn);
            ServiceResponse<List<NewProduct>> result = productData.getList(category, page, 5);
            if (result.getCode() == MessageCode.SUCCESS) {
                if (adapter == null) {
                    productDataList = result.getData();
                    adapter = new MobileAdapter(productDataList, getApplicationContext());
                    recycleViewMobile.setAdapter(adapter);
                } else {
                    int pos = productDataList.size() - 1;
                    int total = result.getData().size();
                    for (int i = 0; i < total; i++) {
                        productDataList.add(result.getData().get(i));
                    }
                    adapter.notifyItemRangeInserted(pos, total);
                }
            } else {
                Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                isLoading = true;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void actionToolBar() {
        setSupportActionBar(toolbarMobile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Laptop");
        toolbarMobile.setTitleTextColor(getResources().getColor(R.color.white));
        toolbarMobile.setNavigationIcon(R.drawable.baseline_menu_24);
        toolbarMobile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayoutMobile.openDrawer(GravityCompat.START);
            }
        });
    }

    public static void redirectActivity(Activity currActivity, Class secondActivity) {
        Intent intent = new Intent(currActivity, secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        currActivity.startActivity(intent);
        currActivity.finish();
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayoutMobile);
    }

    public static void restartActivity(Activity activity) {
        if (Build.VERSION.SDK_INT >= 11) {
            activity.recreate();
        } else {
            activity.finish();
            activity.startActivity(activity.getIntent());
        }
    }
}